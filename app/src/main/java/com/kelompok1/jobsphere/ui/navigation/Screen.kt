package com.kelompok1.jobsphere.ui.navigation

sealed class Screen(val route: String) {
    // Object Utama
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Register : Screen("register")

    // Object JobSeeker
    object JobSeekerHomePage : Screen("jobSeekerHomePage/{username}") {
        fun createRoute(username: String) = "jobSeekerHomePage/$username"
    }

    // Object Company
    object CompanyHomePage : Screen("companyHomePage/{username}") {
        fun createRoute(username: String) = "companyHomePage/$username"
    }
    object AddJobPage : Screen("addJobPage")
    object JobHistoryCompany : Screen("jobHistoryCompany")
    object CompanyProfile : Screen("companyProfile")
    object JobHistoryCompanyView : Screen("jobHistoryCompanyView/{jobId}") {
        fun createRoute(jobId: String) = "jobHistoryCompanyView/$jobId"
    }
    object JobView : Screen("jobView/{jobId}") {
        fun createRoute(jobId: String) = "jobView/$jobId"
    }
}


