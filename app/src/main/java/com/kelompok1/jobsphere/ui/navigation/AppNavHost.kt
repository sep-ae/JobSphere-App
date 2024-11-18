package com.kelompok1.jobsphere.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelompok1.jobsphere.ViewModel.AuthState
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ui.company.AddJobPage
import com.kelompok1.jobsphere.ui.company.CompanyHomePage
import com.kelompok1.jobsphere.ui.jobseeker.JobSeekerHomePage
import com.kelompok1.jobsphere.ui.screen.LandingScreen
import com.kelompok1.jobsphere.ui.screen.LoginPage
import com.kelompok1.jobsphere.ui.screen.RegisterPage
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    // Observasi status autentikasi dari ViewModel
    val authState by authViewModel.authState.observeAsState()

    // Tentukan rute awal berdasarkan status autentikasi
    val startDestination = when (authState) {
        is AuthState.Authenticated -> {
            val role = (authState as AuthState.Authenticated).role
            if (role == "jobseeker") Screen.JobSeekerHomePage.route else Screen.CompanyHomePage.route
        }
        else -> Screen.Landing.route
    }

    // NavHost dengan rute dinamis
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Landing.route) {
            LandingScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Login.route) {
            LoginPage(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Register.route) {
            RegisterPage(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.JobSeekerHomePage.route) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            JobSeekerHomePage(
                navController = navController,
                username = username,
                userViewModel = userViewModel,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(Screen.CompanyHomePage.route) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            CompanyHomePage(
                navController = navController,
                username = username,
                userViewModel = userViewModel,
                drawerState = drawerState,
                scope = scope
            )
        }
        composable(Screen.AddJobPage.route) {
            AddJobPage(navController = navController)
        }
    }
}
