package com.kelompok1.jobsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ui.navigation.AppNavHost
import com.kelompok1.jobsphere.ui.theme.JobSphereTheme
import androidx.compose.runtime.rememberCoroutineScope

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Using SplashScreen
        installSplashScreen().apply {
            setKeepOnScreenCondition { false } // SplashScreen disappears immediately
        }

        setContent {
            JobSphereTheme {
                // Create the drawerState and scope to pass them to AppNavHost
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // Corrected type
                val scope = rememberCoroutineScope()

                // Pass both authViewModel, userViewModel, drawerState, and scope to AppNavHost
                AppNavHost(
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        }
    }
}
