package com.kelompok1.jobsphere.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.AuthViewModel
import com.kelompok1.jobsphere.ui.theme.RighteousFamily
import com.kelompok1.jobsphere.ui.theme.Grey
import com.kelompok1.jobsphere.ui.component.PrivacyPolicyDialog

@Composable
fun RegisterPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()

    // Helper function to validate email
    fun validateEmail(): Boolean {
        return if (email.isEmpty()) {
            emailError = "Email cannot be empty"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            false
        } else {
            emailError = ""
            true
        }
    }

    // Helper function to validate password
    fun validatePassword(): Boolean {
        return if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            false
        } else {
            passwordError = ""
            true
        }
    }

    // Helper function to validate confirm password
    fun validateConfirmPassword(): Boolean {
        return if (password != confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            false
        } else {
            confirmPasswordError = ""
            true
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
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.navigate("landing") }
                    .size(24.dp)
            )

            Text(
                text = "JobSphere",
                fontSize = 24.sp,
                fontFamily = RighteousFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Register",
            fontSize = 20.sp,
            fontFamily = RighteousFamily,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Username Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Username", fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                shape = RoundedCornerShape(35.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Grey,
                    focusedBorderColor = Grey,
                    unfocusedBorderColor = Grey
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                textStyle = MaterialTheme.typography.body1.copy(fontSize = 14.sp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Email", fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(35.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Grey,
                    focusedBorderColor = Grey,
                    unfocusedBorderColor = Grey
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                textStyle = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                isError = emailError.isNotEmpty()
            )
            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Password", fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                shape = RoundedCornerShape(35.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Grey,
                    focusedBorderColor = Grey,
                    unfocusedBorderColor = Grey
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                textStyle = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                isError = passwordError.isNotEmpty()
            )
            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Confirm Password", fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                shape = RoundedCornerShape(35.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Grey,
                    focusedBorderColor = Grey,
                    unfocusedBorderColor = Grey
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                textStyle = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                    }
                },
                isError = confirmPasswordError.isNotEmpty()
            )
            if (confirmPasswordError.isNotEmpty()) {
                Text(
                    text = confirmPasswordError,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Terms and Conditions Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            Checkbox(
                checked = agreeToTerms,
                onCheckedChange = { agreeToTerms = it },
                colors = CheckboxDefaults.colors(checkmarkColor = MaterialTheme.colors.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "I agree to the Terms and Conditions and JobSphere Privacy Policy",
                modifier = Modifier.clickable { showPrivacyPolicyDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register Btn
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (validateEmail() && validatePassword() && validateConfirmPassword() && agreeToTerms) {
                        authViewModel.signup(email, password, "job_seeker", username)
                        navController.navigate("login")
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = agreeToTerms,
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Job Seeker")
            }

            Button(
                onClick = {
                    if (validateEmail() && validatePassword() && validateConfirmPassword() && agreeToTerms) {
                        authViewModel.signup(email, password, "company", username)
                        navController.navigate("login")
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = agreeToTerms,
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Company")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Already have an account? Login",
            modifier = Modifier
                .clickable { navController.navigate("login") }
                .padding(vertical = 16.dp),
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }

    if (showPrivacyPolicyDialog) {
        PrivacyPolicyDialog(
            onDismiss = { showPrivacyPolicyDialog = false },
            onAgree = { agreeToTerms = true; showPrivacyPolicyDialog = false },
            onDisagree = { agreeToTerms = false; showPrivacyPolicyDialog = false }
        )
    }
}
