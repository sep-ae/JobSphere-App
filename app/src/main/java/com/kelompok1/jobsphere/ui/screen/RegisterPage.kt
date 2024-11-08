package com.kelompok1.jobsphere.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.AuthViewModel

@Composable
fun RegisterPage(navController: NavController, modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }  // New state for the username

    val authState = authViewModel.authState.observeAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Register", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Username Field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for registering as Job Seeker or Company
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    authViewModel.signup(email, password, "job_seeker", username) // Register as Job Seeker with username
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Register as Job Seeker")
            }

            Button(
                onClick = {
                    authViewModel.signup(email, password, "company", username) // Register as Company with username
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Register as Company")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text = "Already have an account? Login")
        }
    }
}
