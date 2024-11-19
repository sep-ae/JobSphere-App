package com.kelompok1.jobsphere.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.AuthState
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ui.theme.Grey
import com.kelompok1.jobsphere.ui.theme.RighteousFamily

@Composable
fun LoginPage(navController: NavController, modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginAttempted by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // State untuk Auth
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        if (isLoginAttempted) {
            when (authState.value) {
                is AuthState.Authenticated -> {
                    // Ambil role dan username setelah login sukses
                    val role = (authState.value as AuthState.Authenticated).role
                    val username = (authState.value as AuthState.Authenticated).username

                    // Navigasi berdasarkan role
                    val destination = when (role) {
                        "job_seeker" -> "JobSeekerHome/$username"
                        "company" -> "CompanyHome/$username"
                        else -> "Login" // Fallback jika role tidak dikenali
                    }

                    navController.navigate(destination) {
                        // Hapus halaman login dari backstack supaya pengguna tidak bisa kembali ke halaman login
                        popUpTo("Login") { inclusive = true }
                    }
                    isLoginAttempted = false
                }
                is AuthState.Error -> {
                    // Tampilkan pesan error jika terjadi kesalahan
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
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with JobSphere title and back icon
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            androidx.compose.material.Text(
                text = "JobSphere",
                fontSize = 24.sp,
                fontFamily = RighteousFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(120.dp))

        androidx.compose.material.Text(
            text = "Sign Up",
            fontSize = 20.sp,
            fontFamily = RighteousFamily,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Email Field
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            androidx.compose.material.OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                shape = RoundedCornerShape(35.dp),
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Grey,
                    focusedBorderColor = Grey,
                    unfocusedBorderColor = Grey
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            androidx.compose.material.OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                shape = RoundedCornerShape(35.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Grey,
                    focusedBorderColor = Grey,
                    unfocusedBorderColor = Grey
            )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.login(email, password) // Mulai proses login
            isLoginAttempted = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A0DAD))) {
            Text("SignUp")
        }

        Spacer(modifier = Modifier.height(250.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text(text = "Don’t have an account? Register")
        }
    }
}
