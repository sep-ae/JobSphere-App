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

// Enum untuk peran pengguna
enum class UserRole(val value: String) {
    JobSeeker("jobseeker"),
    Company("company"),
    Guest("guest"); // Tambahkan peran Guest

    companion object {
        fun fromString(value: String): UserRole? {
            return values().firstOrNull { it.value.equals(value, ignoreCase = true) }
        }
    }
}

// Status hasil operasi
sealed class ResultState<out T> {
    object Idle : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Failure(val error: String) : ResultState<Nothing>()
}

class JobViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _jobState = MutableStateFlow<ResultState<List<Job>>>(ResultState.Idle)
    val jobState: StateFlow<ResultState<List<Job>>> = _jobState

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    private val _filteredJobs = MutableStateFlow<List<Job>>(emptyList())
    val filteredJobs: StateFlow<List<Job>> = _filteredJobs

    // Ambil pekerjaan berdasarkan role
    fun fetchJobs(role: UserRole) {
        viewModelScope.launch {
            resetJobState() // Bersihkan state sebelum fetch baru
            _jobState.value = ResultState.Loading

            try {
                val query = getQueryForRole(role)
                val snapshot = query.get().await()

                val jobList = snapshot.documents.mapNotNull { document ->
                    val job = document.toObject(Job::class.java)
                    job?.copy(id = document.id)
                }
                _jobs.value = jobList
                _jobState.value = ResultState.Success(jobList)
            } catch (e: Exception) {
                _jobState.value = ResultState.Failure("Failed to fetch jobs: ${e.message ?: "Unknown error"}")
            }
        }
    }

    // Dapatkan query berdasarkan role
    private fun getQueryForRole(role: UserRole) = when (role) {
        UserRole.JobSeeker, UserRole.Guest -> firestore.collection("jobs") // Guest dan JobSeeker mendapatkan semua pekerjaan
        UserRole.Company -> {
            val userId = auth.currentUser?.uid
                ?: throw IllegalStateException("User not authenticated")
            firestore.collection("jobs").whereEqualTo("userId", userId)
        }
    }

    // Cari pekerjaan berdasarkan query
    fun searchJobs(query: String) {
        _filteredJobs.value = _jobs.value.filter { job ->
            job.title.contains(query, ignoreCase = true) ||
                    job.description.contains(query, ignoreCase = true) ||
                    job.location.contains(query, ignoreCase = true)
        }
    }

    // Filter pekerjaan berdasarkan kategori
    fun fetchJobsByCategory(category: String) {
        viewModelScope.launch {
            _jobState.value = ResultState.Loading
            try {
                val query = if (category.equals("All jobs", ignoreCase = true)) {
                    firestore.collection("jobs")
                } else {
                    firestore.collection("jobs").whereEqualTo("jobCategory", category)
                }

                val snapshot = query.get().await()
                val jobList = snapshot.documents.mapNotNull { document ->
                    val job = document.toObject(Job::class.java)
                    job?.copy(id = document.id)
                }

                _filteredJobs.value = jobList
                _jobState.value = ResultState.Success(jobList)
            } catch (e: Exception) {
                _jobState.value = ResultState.Failure("Failed to fetch jobs by category: ${e.message ?: "Unknown error"}")
            }
        }
    }

    // Tambahkan pekerjaan baru
    fun addJob(
        title: String,
        description: String,
        location: String,
        salary: String,
        qualifications: String,
        jobType: String,
        jobCategory: String,
        minEducation: String,
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
            jobCategory = jobCategory,
            minEducation = minEducation,
            started = started,
            deadline = deadline
        )

        viewModelScope.launch {
            _jobState.value = ResultState.Loading
            try {
                val jobRef = firestore.collection("jobs").add(job).await()
                val newJob = job.copy(id = jobRef.id)
                _jobs.value = _jobs.value + newJob
                _jobState.value = ResultState.Success(_jobs.value)
            } catch (e: Exception) {
                _jobState.value = ResultState.Failure("Failed to add job: ${e.message ?: "Unknown error"}")
            }
        }
    }

    // Hapus pekerjaan
    fun deleteJob(jobId: String) {
        viewModelScope.launch {
            _jobState.value = ResultState.Loading
            try {
                firestore.collection("jobs").document(jobId).delete().await()

                val updatedJobs = _jobs.value.filterNot { it.id == jobId }
                _jobs.value = updatedJobs

                _jobState.value = ResultState.Success(updatedJobs)
            } catch (e: Exception) {
                _jobState.value = ResultState.Failure("Failed to delete job: ${e.message ?: "Unknown error"}")
            }
        }
    }

    // Fungsi untuk menghitung jumlah pelamar untuk pekerjaan tertentu
    fun getApplicantsCount(jobId: String): StateFlow<Int> {
        val applicantsCountState = MutableStateFlow(0)

        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("applications")
                    .whereEqualTo("jobId", jobId)
                    .get()
                    .await()

                // Hitung jumlah dokumen (pelamar)
                applicantsCountState.value = snapshot.size()
            } catch (e: Exception) {
                applicantsCountState.value = 0
            }
        }
        return applicantsCountState
    }

    // Reset semua state pekerjaan
    fun resetJobState() {
        _jobs.value = emptyList()
        _filteredJobs.value = emptyList()
        _jobState.value = ResultState.Idle
    }
}
