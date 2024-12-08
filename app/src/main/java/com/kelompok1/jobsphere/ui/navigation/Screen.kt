package com.kelompok1.jobsphere.ui.navigation

sealed class Screen(val route: String) {
    // Object Utama
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Register : Screen("register")
    object SettingsScreen : Screen("SettingsScreen")
    object ExploreJob : Screen("ExploreJob")
    object GuestPage : Screen("GuestPage")

    // Object JobSeeker
    object JobSeekerHomePage : Screen("jobSeekerHomePage/{username}") {
        fun createRoute(username: String) = "jobSeekerHomePage/$username"
    }
    object JobSeekerProfile : Screen("jobSeekerProfile")
    object EditJobSeekerProfile : Screen("editJobSeekerProfile/{userId}") {
        fun createRoute(userId: String) = "editJobSeekerProfile/$userId"
    }
    object JobSeekerHistory : Screen("jobSeekerHistory")
    object JobDetailView : Screen("jobDetailView/{jobId}") {
        fun createRoute(jobId: String) = "jobDetailView/$jobId"
    }
    object ApplicationHistory : Screen("applicationHistory")

    object JobCategoryView : Screen("jobCategoryView/{category}") {
        fun createRoute(category: String) = "jobCategoryView/$category"
    }
    object JobSeekerProgress : Screen("jobSeekerProgress/{userId}") {
        fun createRoute(userId: String) = "jobSeekerProgress/$userId"
    }
    object JobseekerNotification : Screen("JobseekerNotification")

    // Object Company
    object CompanyHomePage : Screen("companyHomePage/{username}") {
        fun createRoute(username: String) = "companyHomePage/$username"
    }
    object AddJobPage : Screen("addJobPage")
    object JobHistoryCompany : Screen("jobHistoryCompany")
    object CompanyProfile : Screen("companyProfile/{companyId}") {
        fun createRoute(companyId: String) = "companyProfile/$companyId"
    }

    object EditCompanyProfile : Screen("editCompanyProfile/{companyId}") {
        fun createRoute(companyId: String) = "editCompanyProfile/$companyId"
    }
    object JobHistoryCompanyView : Screen("jobHistoryCompanyView/{jobId}") {
        fun createRoute(jobId: String) = "jobHistoryCompanyView/$jobId"
    }
    object JobView : Screen("jobView/{jobId}") {
        fun createRoute(jobId: String) = "jobView/$jobId"
    }
}


