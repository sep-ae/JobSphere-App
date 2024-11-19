package com.kelompok1.jobsphere.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompok1.jobsphere.data.model.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Failure(val error: String) : ResultState<Nothing>()
}

class JobViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _jobState = MutableStateFlow<ResultState<Unit>>(ResultState.Loading)
    val jobState: StateFlow<ResultState<Unit>> = _jobState

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    fun addJob(
        title: String,
        description: String,
        location: String,
        salary: String,
        qualifications: String,
        jobType: String,
        started: Long,
        deadline: Long
    ) {
        if (title.isBlank() || description.isBlank() || location.isBlank() || salary.isBlank()) {
            _jobState.value = ResultState.Failure("All fields must be filled!")
            return
        }

        val salaryValue = salary.toDoubleOrNull()
        if (salaryValue == null || salaryValue < 0) {
            _jobState.value = ResultState.Failure("Invalid salary value!")
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            _jobState.value = ResultState.Failure("User not authenticated!")
            return
        }

        val job = Job(
            title = title,
            description = description,
            location = location,
            salary = salaryValue,
            userId = userId,
            qualifications = qualifications.split(",").map { it.trim() },
            jobType = jobType,
            started = started,
            deadline = deadline

        )

        viewModelScope.launch {
            _jobState.value = ResultState.Loading
            try {
                firestore.collection("jobs").add(job).await()

                _jobState.value = ResultState.Success(Unit)
            } catch (e: Exception) {
                _jobState.value = ResultState.Failure("Failed to add job: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun fetchJobs(forCompany: Boolean = false) {
        viewModelScope.launch {
            _jobState.value = ResultState.Loading

            try {
                val query = if (forCompany) {
                    firestore.collection("jobs")
                        .whereEqualTo("userId", auth.currentUser?.uid)
                } else {
                    firestore.collection("jobs")
                }

                val snapshot = query.get().await()
                val jobList = snapshot.documents.mapNotNull { it.toObject(Job::class.java) }
                _jobs.value = jobList
                _jobState.value = ResultState.Success(Unit)
            } catch (e: Exception) {
                _jobState.value = ResultState.Failure("Failed to fetch jobs: ${e.message ?: "Unknown error"}")
            }
        }
    }
}
