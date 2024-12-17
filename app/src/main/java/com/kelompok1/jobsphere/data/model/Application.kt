package com.kelompok1.jobsphere.data.model

data class Application(
    val id: String = "",
    val userId: String = "",
    val jobId: String = "",
    val appliedAt: Long = System.currentTimeMillis(),
    val status: String = "Pending"
)
