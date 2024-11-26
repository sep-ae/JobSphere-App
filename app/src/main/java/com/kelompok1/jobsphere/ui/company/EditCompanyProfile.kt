package com.kelompok1.jobsphere.ui.company

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.CompanyProfileViewModel

@Composable
fun EditCompanyProfile(
    navController: NavController,
    companyId: String,
    viewModel: CompanyProfileViewModel
) {
    val profile by viewModel.companyProfileState.collectAsState()
    var companyName by remember { mutableStateOf(profile.name) }
    var email by remember { mutableStateOf(profile.email) }
    var address by remember { mutableStateOf(profile.address) }
    var phone by remember { mutableStateOf(profile.phone) }
    var description by remember { mutableStateOf(profile.description) }
    var industrialSector by remember { mutableStateOf(profile.industrialSector) }
    var visionAndMission by remember { mutableStateOf(profile.visionAndMission) }
    var benefitsAndFacilities by remember { mutableStateOf(profile.benefitsAndFacilities) }
    var socialMedia by remember { mutableStateOf(profile.socialMedia) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(value = companyName, onValueChange = { companyName = it }, label = { Text("Company Name") })
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = address, onValueChange = { address = it }, label = { Text("Address") })
        TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
        TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        TextField(value = industrialSector, onValueChange = { industrialSector = it }, label = { Text("Industrial Sector") })
        TextField(value = visionAndMission, onValueChange = { visionAndMission = it }, label = { Text("Vision and Mission") })
        TextField(value = benefitsAndFacilities, onValueChange = { benefitsAndFacilities = it }, label = { Text("Benefits and Facilities") })
        TextField(value = socialMedia, onValueChange = { socialMedia = it }, label = { Text("Social Media") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val updatedProfile = com.kelompok1.jobsphere.ViewModel.CompanyProfile(
                companyName,
                email,
                address,
                phone,
                description,
                industrialSector,
                visionAndMission,
                benefitsAndFacilities,
                socialMedia
            )
            viewModel.saveCompanyProfile(companyId, updatedProfile) // Menyimpan data ke Firestore
            navController.popBackStack() // Kembali ke tampilan profil
        }) {
            Text("Save")
        }
    }
}
