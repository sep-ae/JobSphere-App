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

    private val _notifications = MutableStateFlow<List<Application>>(emptyList()) // Notifications state
    val notifications: StateFlow<List<Application>> = _notifications

    // Fungsi di ApplicationViewModel untuk mengambil aplikasi berdasarkan userId
    fun fetchApplicationsByUser() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("ApplicationViewModel", "User not logged in.")
            return
        }

        viewModelScope.launch {
            _applications.value = emptyList() // Atur ulang daftar aplikasi sebelum memuat data baru.
            try {
                val snapshot = firestore.collection("applications")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()
                val applicationList = snapshot.documents.mapNotNull { document ->
                    document.toObject(Application::class.java)?.copy(id = document.id)
                }
                _applications.value = applicationList
                // Check for status updates and notify the UI
                checkForNotifications(applicationList)
            } catch (e: Exception) {
                Log.e("ApplicationViewModel", "Error fetching applications: ${e.message}")
            }
        }
    }

    // Function to check for status updates and push to notifications
    private fun checkForNotifications(applications: List<Application>) {
        val newNotifications = applications.filter { application ->
            application.status == "Accepted" || application.status == "Rejected"
        }
        if (newNotifications.isNotEmpty()) {
            _notifications.value = newNotifications
        }
    }

    // Fungsi untuk mengirim aplikasi ke Firestore
    fun submitApplication(jobId: String, coverLetter: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _applicationState.value = ApplicationResultState.Failure("User not authenticated!")
            return
        }

        if (coverLetter.isBlank()) {
            _applicationState.value = ApplicationResultState.Failure("Cover letter cannot be empty!")
            return
        }

        val application = Application(
            userId = userId,
            jobId = jobId,
            coverLetter = coverLetter,
            appliedAt = System.currentTimeMillis(),
            status = "Pending"
        )

        viewModelScope.launch {
            _applicationState.value = ApplicationResultState.Loading
            try {
                val applicationRef = firestore.collection("applications").add(application).await()
                _applicationState.value = ApplicationResultState.Success(_applications.value) // Returning the list of applications after submission
                fetchApplicationsByUser() // Refresh application list after success
            } catch (e: Exception) {
                _applicationState.value = ApplicationResultState.Failure("Failed to submit application: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun hasApplied(jobId: String): Boolean {
        return _applications.value.any { it.jobId == jobId }
    }

    // Listen for application status updates in Firestore (from Company)
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
                    // Check for new status updates
                    checkForNotifications(updatedApplications)
                }
            }
    }

    // Reset state aplikasi
    fun resetApplicationState() {
        _applicationState.value = ApplicationResultState.Idle
    }
}
