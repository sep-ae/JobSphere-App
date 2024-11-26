package com.kelompok1.jobsphere.ui.jobseeker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.kelompok1.jobsphere.ViewModel.ProfileViewModel


@Composable
fun JobSeekerProfile(
    viewModel: ProfileViewModel,
    navController: NavHostController
) {
    val profile by viewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Profile", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Username: ${profile.username.ifEmpty { "Not provided" }}")
        Text("Email: ${profile.email.ifEmpty { "Not provided" }}")
        Text("Address: ${profile.address.ifEmpty { "Not provided" }}")
        Text("Personal Summary: ${profile.personalSummary.ifEmpty { "Not provided" }}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("editJobSeekerProfile")
        }) {
            Text("Edit Profile")
        }
    }
}


