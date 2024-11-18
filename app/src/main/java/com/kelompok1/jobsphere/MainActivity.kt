package com.kelompok1.jobsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.kelompok1.jobsphere.ViewModel.AuthState
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ui.navigation.AppNavHost
import com.kelompok1.jobsphere.ui.navigation.Screen
import com.kelompok1.jobsphere.ui.theme.JobSphereTheme
import androidx.compose.runtime.livedata.observeAsState

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SplashScreen logic
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                val authState = authViewModel.authState.value
                authState == null || authState is AuthState.Loading
            }
        }

        setContent {
            JobSphereTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val authState by authViewModel.authState.observeAsState()

                val navController = rememberNavController()

                LaunchedEffect(authState) {
                    when (authState) {
                        is AuthState.Authenticated -> {
                            // Safe cast to Authenticated type
                            val authenticatedState = authState as? AuthState.Authenticated
                            authenticatedState?.let {
                                val username = it.username
                                val role = it.role
                                when (role) {
                                    "jobseeker" -> {
                                        navController.navigate(Screen.JobSeekerHomePage.createRoute(username)) {
                                            popUpTo(Screen.Landing.route) { inclusive = true }
                                        }
                                    }
                                    "company" -> {
                                        navController.navigate(Screen.CompanyHomePage.createRoute(username)) {
                                            popUpTo(Screen.Landing.route) { inclusive = true }
                                        }
                                    }
                                }
                            }
                        }
                        is AuthState.Unauthenticated -> {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Landing.route) { inclusive = true }
                            }
                        }
                        is AuthState.Loading -> {
                            navController.navigate(Screen.Landing.route) {
                                popUpTo(Screen.Landing.route) { inclusive = true }
                            }
                        }
                        else -> {
                            // Handle unexpected cases (e.g., Error or null)
                        }
                    }
                }

                AppNavHost(
                    navController = navController,
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        }
    }
}
