package com.kelompok1.jobsphere.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.kelompok1.jobsphere.R
import kotlinx.coroutines.*

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreenContent()
        }

        // Menggunakan lifecycleScope untuk menjalankan coroutine
        lifecycleScope.launch {
            delay(1000)  // Splash screen selama 1 detik
            val intent = Intent(this@SplashScreenActivity, LandingScreenActivity::class.java)
            startActivity(intent)
            finish()  // Menutup SplashScreenActivity setelah berpindah ke MainActivity
        }
    }
}

@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo JobSphere",
            modifier = Modifier.size(80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreenContent()
}
