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

    // ObjectCompany
    object CompanyHomePage : Screen("companyHomePage/{username}") {
        fun createRoute(username: String) = "companyHomePage/$username"
    }
    object AddJobPage : Screen("addJobPage")
    object JobHistoryCompany : Screen("jobHistoryCompany")
    object CompanyProfile : Screen("companyProfile")

}

