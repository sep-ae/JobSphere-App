// LandingScreenActivity.kt

package com.kelompok1.jobsphere.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.kelompok1.jobsphere.R
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ui.theme.JobSphereTheme
import com.kelompok1.jobsphere.ui.theme.RighteousFamily

class LandingScreenActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobSphereTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}

// Fungsi Navigasi
@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") {
            LandingScreen(
                onExploreClick = { navController.navigate("home") },
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable("home") { HomePage() }
        composable("login") { LoginPage(navController = navController, authViewModel = authViewModel) }
        composable("register") { RegisterPage(navController = navController, authViewModel = authViewModel) }
    }
}

// LandingScreen composable
@Composable
fun LandingScreen(
    onExploreClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "JobSphere",
            fontSize = 16.sp,
            fontFamily = RighteousFamily
        )

        Spacer(modifier = Modifier.height(100.dp))

        Button(
            onClick = onExploreClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A0DAD))
        ) {
            Text("Explore Job", color = Color.White, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Login", color = Color.White, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onRegisterClick) {
            Text(text = "Don’t have an account? Register")
        }
    }
}

// HomePage composable
@Composable
fun HomePage() {
    Text("Welcome to Home Page!")
}

// Preview LandingScreen
@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    JobSphereTheme {
        LandingScreen()
    }
}
