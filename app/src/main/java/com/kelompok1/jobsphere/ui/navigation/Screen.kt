package com.kelompok1.jobsphere.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Register : Screen("register")

    // Rute dengan argumen dinamis
    object JobSeekerHomePage : Screen("jobSeekerHomePage/{username}") {
        fun createRoute(username: String) = "jobSeekerHomePage/$username"
    }

    object CompanyHomePage : Screen("companyHomePage/{username}") {
        fun createRoute(username: String) = "companyHomePage/$username"
    }

    object AddJobPage : Screen("addJobPage")
}
