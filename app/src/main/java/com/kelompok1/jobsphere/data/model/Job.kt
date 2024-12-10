package com.kelompok1.jobsphere.data.model

data class Job(
    val id : String ="",
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val userId: String = "",
    val started: Long = System.currentTimeMillis(),
    val salary: Double = 0.0,
    val qualifications: List<String> = emptyList(),
    val jobType: String = "Full-Time",
    val deadline: Long = System.currentTimeMillis(),
    val jobCategory: String = "",
    val minEducation: String= ""
)
