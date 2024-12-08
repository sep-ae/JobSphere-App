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
import com.kelompok1.jobsphere.ViewModel.ProfileViewModel
import com.kelompok1.jobsphere.ui.theme.RighteousFamily

@Composable
fun JobSeekerProfile(
    viewModel: ProfileViewModel,
    navController: NavHostController
) {
    // State untuk mengelola tampilan overlay dan kategori aktif
    var isEditingAddress by remember { mutableStateOf(false) }
    var editedAddress by remember { mutableStateOf("") }
    var isEditingSummary by remember { mutableStateOf(false) }
    var editedSummary by remember { mutableStateOf("") }
    var isEditingCareerHistory by remember { mutableStateOf(false) }
    var editedCareerHistory by remember { mutableStateOf("") }
    var isEditingEducation by remember { mutableStateOf(false) }
    var editedEducation by remember { mutableStateOf("") }
    var isEditingSkill by remember { mutableStateOf(false) }
    var editedSkill by remember { mutableStateOf("") }
    var isEditingLanguage by remember { mutableStateOf(false) }
    var editedLanguage by remember { mutableStateOf("") }

    // Observasi profil dari ViewModel
    val profile by viewModel.profileState.collectAsState()
    val username by viewModel.username.collectAsState()
    val useremail by viewModel.useremail.collectAsState()

    // Fetch data profil saat pertama kali Composable dipanggil
    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
        viewModel.fetchProfile()

        // Inisialisasi nilai awal dari ViewModel
        editedSummary = profile.personalSummary
        editedCareerHistory = profile.careerHistory.joinToString(", ") // Mengubah list menjadi string
        editedEducation = profile.education.joinToString(", ")
        editedSkill = profile.skill.joinToString(", ")
        editedLanguage = profile.language.joinToString(", ")
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
            Text("Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                            text = profile.username.ifEmpty { "$username" },
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
                                text = profile.email.ifEmpty { "$useremail" },
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
                                    viewModel.saveProfile(profile.copy(address = editedAddress))
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
                                    editedAddress = profile.address
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
                                text = profile.address.ifEmpty { "Not provided" },
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


            // **Integrasi Personal Summary**
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
                        text = "Personal Summary",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingSummary) {
                        Column {
                            TextField(
                                value = editedSummary,
                                onValueChange = { editedSummary = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your personal summary") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(profile.copy(personalSummary = editedSummary))
                                    isEditingSummary = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingSummary = false
                                    editedSummary = profile.personalSummary
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
                                text = profile.personalSummary.ifEmpty { "Not provided" },
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { isEditingSummary = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Personal Summary"
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Career History
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
                            text = "Career History",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        // Menampilkan tombol Edit jika tidak sedang mengedit
                        if (!isEditingCareerHistory) {
                            IconButton(onClick = { isEditingCareerHistory = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Career History"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingCareerHistory) {
                        Column {
                            TextField(
                                value = editedCareerHistory,
                                onValueChange = { editedCareerHistory = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your career history") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    // Simpan perubahan
                                    viewModel.saveProfile(
                                        profile.copy(careerHistory = editedCareerHistory.split(", "))
                                    )
                                    isEditingCareerHistory = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    // Batalkan perubahan
                                    isEditingCareerHistory = false
                                    editedCareerHistory = profile.careerHistory.joinToString(", ")
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
                        // Menampilkan Career History saat tidak mengedit
                        if (profile.careerHistory.isNotEmpty()) {
                            Column {
                                profile.careerHistory.forEach { history ->
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
                                            text = history,
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

// Edit Education
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
                            text = "Education",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        if (!isEditingEducation) {
                            IconButton(onClick = { isEditingEducation = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Education"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingEducation) {
                        Column {
                            TextField(
                                value = editedEducation,
                                onValueChange = { editedEducation = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your education") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(
                                        profile.copy(education = editedEducation.split(", "))
                                    )
                                    isEditingEducation = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingEducation = false
                                    editedEducation = profile.education.joinToString(", ")
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
                        if (profile.education.isNotEmpty()) {
                            Column {
                                profile.education.forEach { edu ->
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
                                            text = edu,
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

// Edit Skill
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
                            text = "Skill",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        if (!isEditingSkill) {
                            IconButton(onClick = { isEditingSkill = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Skill"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingSkill) {
                        Column {
                            TextField(
                                value = editedSkill,
                                onValueChange = { editedSkill = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your skill") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    viewModel.saveProfile(
                                        profile.copy(skill = editedSkill.split(", "))
                                    )
                                    isEditingSkill = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    isEditingSkill = false
                                    editedSkill = profile.skill.joinToString(", ")
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
                        if (profile.skill.isNotEmpty()) {
                            Column {
                                profile.skill.forEach { skill ->
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
                                            text = skill,
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
                    // Baris pertama: Judul dan tombol Edit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Language",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )

                        // Menampilkan tombol Edit hanya jika tidak sedang dalam mode pengeditan
                        if (!isEditingLanguage) {
                            IconButton(onClick = { isEditingLanguage = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Language"
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Mode pengeditan
                    if (isEditingLanguage) {
                        Column {
                            TextField(
                                value = editedLanguage,
                                onValueChange = { editedLanguage = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 14.sp),
                                placeholder = { Text("Enter your language") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                IconButton(onClick = {
                                    // Simpan data baru
                                    viewModel.saveProfile(
                                        profile.copy(
                                            language = editedLanguage.split(", ")
                                        )
                                    )
                                    isEditingLanguage = false // Kembali ke mode normal
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.Green
                                    )
                                }
                                IconButton(onClick = {
                                    // Batalkan pengeditan
                                    isEditingLanguage = false
                                    editedLanguage = profile.language.joinToString(", ") // Reset data
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
                        if (profile.language.isNotEmpty()) {
                            Column {
                                profile.language.forEach { language ->
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
                                            text = language,
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

