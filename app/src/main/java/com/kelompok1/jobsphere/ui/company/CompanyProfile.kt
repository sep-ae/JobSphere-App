package com.kelompok1.jobsphere.ui.jobseeker

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.kelompok1.jobsphere.ViewModel.CompanyProfileViewModel
import com.kelompok1.jobsphere.ui.theme.RighteousFamily

@Composable
fun CompanyProfileScreen(
    viewModel: CompanyProfileViewModel,
    navController: NavHostController
) {
    // State untuk mengelola tampilan overlay dan kategori aktif
    var editedTelephone by remember { mutableStateOf("") }
    var isEditingTelephone by remember { mutableStateOf(false) }
    var editedAddress by remember { mutableStateOf("") }
    var isEditingAddress by remember { mutableStateOf(false) }
    var editedDescription by remember { mutableStateOf("") }
    var isEditingDescription by remember { mutableStateOf(false) }
    var editedIndustrialSector by remember { mutableStateOf("") }
    var isEditingIndustrialSector by remember { mutableStateOf(false) }
    var editedVisionAndMission by remember { mutableStateOf("") }
    var isEditingVisionAndMission by remember { mutableStateOf(false) }
    var editedBenefitsAndFacilities by remember { mutableStateOf("") }
    var isEditingBenefitsAndFacilities by remember { mutableStateOf(false) }
    var editedSocialMedia by remember { mutableStateOf("") }
    var isEditingSocialMedia by remember { mutableStateOf(false) }

    // Observasi profil dari ViewModel
    val companyProfile by viewModel.companyProfileState.collectAsState()
    val username by viewModel.username.collectAsState()
    val useremail by viewModel.useremail.collectAsState()

    // Fetch data profil saat pertama kali Composable dipanggil
    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
        viewModel.fetchCompanyProfile()

        // Inisialisasi nilai awal dari ViewModel
        editedVisionAndMission = companyProfile.visionAndMission.joinToString(", ")
        editedBenefitsAndFacilities = companyProfile.benefitsAndFacilities.joinToString(", ")
        editedSocialMedia = companyProfile.socialMedia.joinToString(", ")
    }


    // Halaman utama profil
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Background halaman
    ) {
        // Bagian tombol kembali (tidak ikut scroll)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Company Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // Bagian konten lainnya yang bisa di-scroll
        Column(modifier = Modifier
            .weight(1f) // Pastikan ini mengisi ruang yang tersisa
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
        ) {
            // Bagian Header Profile dengan foto profil dan informasi (ikut scroll)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                backgroundColor = Color(0xFF0A74DA), // Warna biru untuk latar belakang
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically // Menyatukan elemen secara vertikal
                ) {
                    // Foto Profil di Sebelah Kiri
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                Color.LightGray,
                                CircleShape
                            ), // Warna abu-abu muda untuk lingkaran
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person, // Placeholder icon untuk gambar
                            contentDescription = "Profile Image",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Informasi Profil di Sebelah Kanan
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Nama Pengguna
                        Text(
                            text = companyProfile.username.ifEmpty { "$username" },
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Informasi Email
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Email, // Ikon email
                                contentDescription = "Email Icon",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = companyProfile.email.ifEmpty { "$useremail" },
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Telephone",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingTelephone) {
                        Column {
                            TextField(
                                value = editedTelephone,
                                onValueChange = { editedTelephone = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your telephone number") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(companyProfile.copy(telephone = editedTelephone))
                                    isEditingTelephone = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingTelephone = false
                                    editedTelephone = companyProfile.address
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = companyProfile.telephone.ifEmpty { "Not provided" },
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { isEditingTelephone = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Telephone"
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Address",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingAddress) {
                        Column {
                            TextField(
                                value = editedAddress,
                                onValueChange = { editedAddress = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your address") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(companyProfile.copy(address = editedAddress))
                                    isEditingAddress = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingAddress = false
                                    editedAddress = companyProfile.address
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = companyProfile.address.ifEmpty { "Not provided" },
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { isEditingAddress = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Address"
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Edit Description
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Description",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingDescription) {
                        Column {
                            TextField(
                                value = editedDescription,
                                onValueChange = { editedDescription = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your description") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(companyProfile.copy(description = editedDescription))
                                    isEditingDescription = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingDescription = false
                                    editedDescription = companyProfile.description
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = companyProfile.description.ifEmpty { "Not provided" },
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { isEditingDescription = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Description"
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Industrial Sector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Industrial Sector",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingIndustrialSector) {
                        Column {
                            TextField(
                                value = editedIndustrialSector,
                                onValueChange = { editedIndustrialSector = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your industrial sector") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(companyProfile.copy(industrialSector = editedIndustrialSector))
                                    isEditingIndustrialSector = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingIndustrialSector = false
                                    editedIndustrialSector = companyProfile.industrialSector
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = companyProfile.industrialSector.ifEmpty { "Not provided" },
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { isEditingIndustrialSector = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Industrial Sector"
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Vision And Mission
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Vision And Mission",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        if (!isEditingVisionAndMission) {
                            IconButton(onClick = { isEditingVisionAndMission = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Education"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingVisionAndMission) {
                        Column {
                            TextField(
                                value = editedVisionAndMission,
                                onValueChange = { editedVisionAndMission = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your vision and mission") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(
                                        companyProfile.copy(visionAndMission = editedVisionAndMission.split(", "))
                                    )
                                    isEditingVisionAndMission = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingVisionAndMission = false
                                    editedVisionAndMission = companyProfile.visionAndMission.joinToString(", ")
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    } else {
                        if (companyProfile.visionAndMission.isNotEmpty()) {
                            Column {
                                companyProfile.visionAndMission.forEach { visimisi ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "-",
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = visimisi,
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Not provided",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Benefits And Facilities
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Benefits And Facilities",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        if (!isEditingBenefitsAndFacilities) {
                            IconButton(onClick = { isEditingBenefitsAndFacilities = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Benefits And Facilities"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingBenefitsAndFacilities) {
                        Column {
                            TextField(
                                value = editedBenefitsAndFacilities,
                                onValueChange = { editedBenefitsAndFacilities = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your benefits and facilities") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(
                                        companyProfile.copy(benefitsAndFacilities = editedBenefitsAndFacilities.split(", "))
                                    )
                                    isEditingBenefitsAndFacilities = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingBenefitsAndFacilities = false
                                    editedBenefitsAndFacilities = companyProfile.benefitsAndFacilities.joinToString(", ")
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    } else {
                        if (companyProfile.benefitsAndFacilities.isNotEmpty()) {
                            Column {
                                companyProfile.benefitsAndFacilities.forEach { benefitsAndFacilities ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "-",
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = benefitsAndFacilities,
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Not provided",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Social Media",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )

                        // Menampilkan tombol Edit hanya jika tidak sedang dalam mode pengeditan
                        if (!isEditingSocialMedia) {
                            IconButton(onClick = { isEditingSocialMedia = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Social Media"
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Mode pengeditan
                    if (isEditingSocialMedia) {
                        Column {
                            TextField(
                                value = editedSocialMedia,
                                onValueChange = { editedSocialMedia = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your Social Media") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    // Simpan data baru
                                    viewModel.saveProfile(
                                        companyProfile.copy(
                                            socialMedia = editedSocialMedia.split(", ")
                                        )
                                    )
                                    isEditingSocialMedia = false // Kembali ke mode normal
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    // Batalkan pengeditan
                                    isEditingSocialMedia = false
                                    editedSocialMedia = companyProfile.socialMedia.joinToString(", ") // Reset data
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    } else {
                        // Menampilkan data bahasa saat tidak dalam mode pengeditan
                        if (companyProfile.socialMedia.isNotEmpty()) {
                            Column {
                                companyProfile.socialMedia.forEach { socialmedia ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "@",
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = socialmedia,
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Not provided",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

