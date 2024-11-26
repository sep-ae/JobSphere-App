package com.kelompok1.jobsphere.ui.navigation

import JobHistoryCompany
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelompok1.jobsphere.ViewModel.AuthState
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ViewModel.ProfileViewModel
import com.kelompok1.jobsphere.ViewModel.CompanyProfileViewModel
import com.kelompok1.jobsphere.ui.company.AddJobPage
import com.kelompok1.jobsphere.ui.company.CompanyHomePage
import com.kelompok1.jobsphere.ui.company.CompanyProfile
import com.kelompok1.jobsphere.ui.company.EditCompanyProfile
import com.kelompok1.jobsphere.ui.company.JobHistoryCompanyView
import com.kelompok1.jobsphere.ui.company.JobView
import com.kelompok1.jobsphere.ui.jobseeker.JobSeekerHomePage
import com.kelompok1.jobsphere.ui.jobseeker.JobSeekerProfile
import com.kelompok1.jobsphere.ui.jobseeker.EditJobSeekerProfile
import com.kelompok1.jobsphere.ui.jobseeker.JobSeekerHistory
import com.kelompok1.jobsphere.ui.screen.LandingScreen
import com.kelompok1.jobsphere.ui.screen.LoginPage
import com.kelompok1.jobsphere.ui.screen.RegisterPage
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    // Observasi status autentikasi dari ViewModel
    val authState by authViewModel.authState.observeAsState()
    val profileViewModel: ProfileViewModel = viewModel()
    val companyProfileViewModel: CompanyProfileViewModel = viewModel()

    // Tentukan rute awal berdasarkan status autentikasi
    val startDestination = when (authState) {
        is AuthState.Authenticated -> {
            val role = (authState as AuthState.Authenticated).role
            if (role == "job_seeker") Screen.JobSeekerHomePage.route else Screen.CompanyHomePage.route
        }
        else -> Screen.Landing.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Routing Utama
        composable(Screen.Landing.route) {
            LandingScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Login.route) {
            LoginPage(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Register.route) {
            RegisterPage(navController = navController, authViewModel = authViewModel)
        }

        // Routing JobSeeker
        composable(Screen.JobSeekerHomePage.route) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            JobSeekerHomePage(
                navController = navController,
                username = username,
                userViewModel = userViewModel,
                jobViewModel = jobViewModel,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable("jobSeekerProfile") {
            val profileViewModel: ProfileViewModel = viewModel()
            JobSeekerProfile(
                viewModel = profileViewModel,
                navController = navController
            )
        }
        composable("editJobSeekerProfile") {
            EditJobSeekerProfile(
                viewModel = profileViewModel,
                navController = navController
            )
        }
        composable("JobSeekerHistory") {
            JobSeekerHistory()
        }


        // Routing Company
        composable(Screen.CompanyHomePage.route) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            CompanyHomePage(
                navController = navController,
                username = username,
                userViewModel = userViewModel,
                jobViewModel = jobViewModel,
                drawerState = drawerState,
                scope = scope
            )
        }

        composable(Screen.AddJobPage.route) {
            AddJobPage(
                navController = navController,
                userViewModel = userViewModel,
                jobViewModel = jobViewModel,
                onJobAdded = {
                },
                onError = { errorMessage ->
                }
            )
        }

        composable(Screen.JobHistoryCompany.route) {
            val jobsState by jobViewModel.jobs.collectAsState()

            JobHistoryCompany(
                navController = navController,
                jobs = jobsState,
                onDeleteJob = { jobId -> jobViewModel.deleteJob(jobId) }
            )
        }

        composable("companyProfile/{companyId}") { backStackEntry ->
            val companyId = backStackEntry.arguments?.getString("companyId") ?: ""
            CompanyProfile(
                navController = navController,
                companyId = companyId,
                viewModel = companyProfileViewModel
            )
        }

        composable("editCompanyProfile/{companyId}") { backStackEntry ->
            val companyId = backStackEntry.arguments?.getString("companyId") ?: ""
            EditCompanyProfile(
                navController = navController,
                companyId = companyId,
                viewModel = companyProfileViewModel
            )
        }



        composable(Screen.JobHistoryCompanyView.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            val jobsState by jobViewModel.jobs.collectAsState()

            val job = jobsState.find { it.id == jobId }
            if (job != null) {
                JobHistoryCompanyView(job = job, navController = navController)
            } else {
                Text(text = "Job not found!")
            }
        }

        composable(Screen.JobView.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            val jobsState by jobViewModel.jobs.collectAsState()

            val job = jobsState.find { it.id == jobId }
            if (job != null) {
                JobView(job = job, navController = navController)
            } else {
                Text(text = "Job not found!")
            }
        }
    }
}
