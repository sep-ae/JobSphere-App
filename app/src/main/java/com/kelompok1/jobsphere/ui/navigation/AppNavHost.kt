package com.kelompok1.jobsphere.ui.navigation

import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ui.company.CompanyHomePage
import com.kelompok1.jobsphere.ui.screen.LandingScreen
import com.kelompok1.jobsphere.ui.screen.LoginPage
import com.kelompok1.jobsphere.ui.screen.RegisterPage
import com.kelompok1.jobsphere.ui.jobseeker.JobSeekerHomePage
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route
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

        composable("JobSeekerHome/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            JobSeekerHomePage(
                navController = navController,
                username = username ?: "",
                userViewModel = userViewModel,
                drawerState = drawerState,
                scope = scope
            )
        }

        composable("CompanyHome") {
            // Pass navController to CompanyHomePage
            CompanyHomePage(navController = navController, drawerState = drawerState, scope = scope)
        }
    }
}

