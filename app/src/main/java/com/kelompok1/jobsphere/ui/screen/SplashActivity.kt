package com.kelompok1.jobsphere.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kelompok1.jobsphere.R
import com.kelompok1.jobsphere.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Desain layar splash screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Sesuaikan warna latar belakang
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo JobSphere",
            modifier = Modifier.size(100.dp) // Ukuran logo
        )
    }

    // Logika transisi ke layar berikutnya
    LaunchedEffect(Unit) {
        delay(2000) // Durasi splash screen dalam milidetik (2 detik)
        navController.navigate(Screen.Landing.route) {
            popUpTo(Screen.Splash.route) { inclusive = true } // Hapus layar splash dari stack
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    // Dummy NavHostController untuk preview
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        SplashScreen(navController)
    }
}
