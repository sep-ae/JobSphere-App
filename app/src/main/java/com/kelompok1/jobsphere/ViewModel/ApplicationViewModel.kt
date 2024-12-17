package com.kelompok1.jobsphere.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kelompok1.jobsphere.data.model.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class ApplicationResultState<out T> {
    object Idle : ApplicationResultState<Nothing>()
    object Loading : ApplicationResultState<Nothing>()
    data class Success<out T>(val data: T) : ApplicationResultState<T>()
    data class Failure(val error: String) : ApplicationResultState<Nothing>()
}

class ApplicationViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _applicationState = MutableStateFlow<ApplicationResultState<List<Application>>>(ApplicationResultState.Idle)
    val applicationState: StateFlow<ApplicationResultState<List<Application>>> = _applicationState

    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    val applications: StateFlow<List<Application>> = _applications

    private val _notifications = MutableStateFlow<List<Application>>(emptyList())
    val notifications: StateFlow<List<Application>> = _notifications

    private val _companyUsernames = MutableStateFlow<Map<String, String>>(emptyMap())
    val companyUsernames: StateFlow<Map<String, String>> = _companyUsernames

    fun fetchApplicationsByUser() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("ApplicationViewModel", "User not logged in.")
            return
        }

        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("applications")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()
                val applicationList = snapshot.documents.mapNotNull { document ->
                    document.toObject(Application::class.java)?.copy(id = document.id)
                }
                _applications.value = applicationList

                val jobIds = applicationList.mapNotNull { it.jobId }
                fetchCompanyUsernamesFromJobs(jobIds)

                checkForNotifications(applicationList)
            } catch (e: Exception) {
                Log.e("ApplicationViewModel", "Error fetching applications: ${e.message}")
            }
        }
    }

    private suspend fun fetchCompanyUsernamesFromJobs(jobIds: List<String>) {
        try {
            val usernameMap = mutableMapOf<String, String>()

            for (jobId in jobIds.distinct()) {
                val jobDocument = firestore.collection("jobs").document(jobId).get().await()
                val companyId = jobDocument.getString("userId")

                if (!companyId.isNullOrEmpty()) {
                    val userDocument = firestore.collection("users").document(companyId).get().await()
                    val username = userDocument.getString("username") ?: "Unknown Company"
                    usernameMap[companyId] = username
                }
            }

            _companyUsernames.value = usernameMap
        } catch (e: Exception) {
            Log.e("ApplicationViewModel", "Error fetching company usernames: ${e.message}")
        }
    }


    // Function to check for notifications (Accepted/Rejected statuses)
    private fun checkForNotifications(applications: List<Application>) {
        val newNotifications = applications.filter { application ->
            application.status == "Accepted" || application.status == "Rejected"
        }
        if (newNotifications.isNotEmpty()) {
            _notifications.value = newNotifications
        }
    }

    // Submit an application to Firestore
    fun submitApplication(jobId: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _applicationState.value = ApplicationResultState.Failure("User not authenticated!")
            return
        }


        val application = Application(
            userId = userId,
            jobId = jobId,
            appliedAt = System.currentTimeMillis(),
            status = "Pending"
        )

        viewModelScope.launch {
            _applicationState.value = ApplicationResultState.Loading
            try {
                firestore.collection("applications").add(application).await()
                _applicationState.value = ApplicationResultState.Success(_applications.value)
                fetchApplicationsByUser()
            } catch (e: Exception) {
                _applicationState.value = ApplicationResultState.Failure("Failed to submit application: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun hasApplied(jobId: String): Boolean {
        return _applications.value.any { it.jobId == jobId }
    }

    fun listenForStatusUpdates() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("ApplicationViewModel", "User not logged in.")
            return
        }

        firestore.collection("applications")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ApplicationViewModel", "Error listening to status updates: ${error.message}")
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val updatedApplications = it.documents.mapNotNull { document ->
                        document.toObject(Application::class.java)?.copy(id = document.id)
                    }
                    checkForNotifications(updatedApplications)
                }
            }
    }

    // Reset application state
    fun resetApplicationState() {
        _applicationState.value = ApplicationResultState.Idle
    }
}
