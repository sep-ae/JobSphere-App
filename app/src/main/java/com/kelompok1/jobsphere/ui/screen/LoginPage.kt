package com.kelompok1.jobsphere.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.AuthState
import com.kelompok1.jobsphere.ViewModel.AuthViewModel

@Composable
fun LoginPage(navController: NavController, modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginAttempted by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        if (isLoginAttempted) {
            when (authState.value) {
                is AuthState.Authenticated -> {
                    val role = (authState.value as AuthState.Authenticated).role
                    val username = (authState.value as AuthState.Authenticated).username
                    val destination = when (role) {
                        "job_seeker" -> "JobSeekerHome/$username"
                        "company" -> "CompanyHome/$username"
                        else -> "Login" // Fallback jika role tidak dikenal
                    }
                    navController.navigate(destination) {
                        popUpTo("Login") { inclusive = true } // Hapus halaman login dari backstack
                    }
                    isLoginAttempted = false
                }
                is AuthState.Error -> {
                    Toast.makeText(
                        context,
                        (authState.value as AuthState.Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                    isLoginAttempted = false
                }

                else -> {
                    println("Status tidak dikenali atau sedang loading.")
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            authViewModel.login(email, password) // Memulai proses login
            isLoginAttempted = true
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text(text = "Don’t have an account? Register")
        }
    }
}






