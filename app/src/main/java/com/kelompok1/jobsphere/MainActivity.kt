package com.kelompok1.jobsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
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
            setKeepOnScreenCondition { false }
        }

        setContent {
            JobSphereTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

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
