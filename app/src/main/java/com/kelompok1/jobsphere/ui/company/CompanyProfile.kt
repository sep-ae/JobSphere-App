package com.kelompok1.jobsphere.ui.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.CompanyProfileViewModel

@Composable
fun CompanyProfile(
    navController: NavController,
    companyId: String,
    viewModel: CompanyProfileViewModel
) {
    val profile by viewModel.companyProfileState.collectAsState()

    LaunchedEffect(companyId) {
        viewModel.fetchCompanyProfile(companyId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9EDF1))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bagian Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF2E89FF), shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder untuk foto profil
                    Text(
                        text = "Logo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E89FF)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = profile.name ?: "Company Name",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = profile.email ?: "Email",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bagian Deskripsi dan Informasi Perusahaan
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            ProfileInfoItem("Address", profile.address ?: "Address not available")
            ProfileInfoItem("Phone", profile.phone ?: "Phone not available")
            ProfileInfoItem("Description", profile.description ?: "Description not available")
            ProfileInfoItem("Industrial Sector", profile.industrialSector ?: "Sector not available")
            ProfileInfoItem("Vision and Mission", profile.visionAndMission ?: "Vision and Mission not available")
            ProfileInfoItem("Benefits and Facilities", profile.benefitsAndFacilities ?: "Not available")
            ProfileInfoItem("Social Media", profile.socialMedia ?: "Social Media not available")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Edit Profil
        Button(
            onClick = { navController.navigate("editCompanyProfile/$companyId") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E89FF))
        ) {
            Text("Edit Profile", color = Color.White)
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, color = Color.Black)
    }
}
