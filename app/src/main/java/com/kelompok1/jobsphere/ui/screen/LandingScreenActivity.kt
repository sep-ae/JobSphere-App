package com.kelompok1.jobsphere.ui.screen

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
import androidx.navigation.compose.rememberNavController
import com.kelompok1.jobsphere.R
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ui.theme.RighteousFamily

@Composable
fun LandingScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
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
            onClick = { navController.navigate("ExploreJob") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A0DAD))
        ) {
            Text("Explore Job", color = Color.White, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Login", color = Color.White, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text(
                text = "Don’t have an account? Register",
                color = Color.Black
            )
        }
    }
}

// Preview LandingScreen
@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    // Dummy NavController and AuthViewModel for preview
    val navController = rememberNavController()

    // You can create a dummy AuthViewModel or pass a mock implementation
    val authViewModel = AuthViewModel()

    LandingScreen(navController = navController, authViewModel = authViewModel)
}
