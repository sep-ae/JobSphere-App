package com.kelompok1.jobsphere.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ui.navigation.Screen
import com.kelompok1.jobsphere.ui.theme.DarkBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestPage(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Column for main content
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to JobSphere",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Login Button with height set to 50dp
            Button(
                onClick = { navController.navigate(Screen.Login.route) },
                modifier = Modifier
                    .fillMaxWidth()  // Make the button width fill the available space
                    .height(50.dp)   // Set height to 50dp
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.medium  // Rounded button
            ) {
                Text(text = "Login")
            }

            // Register Button with height set to 50dp
            Button(
                onClick = { navController.navigate(Screen.Register.route) },
                modifier = Modifier
                    .fillMaxWidth()  // Make the button width fill the available space
                    .height(50.dp)   // Set height to 50dp
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.medium  // Rounded button
            ) {
                Text(text = "Register")
            }
        }

        // Top Bar
        TopAppBar(
            title = {
                Text(text = "JobSphere", color = Color.White)
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
    }
}
