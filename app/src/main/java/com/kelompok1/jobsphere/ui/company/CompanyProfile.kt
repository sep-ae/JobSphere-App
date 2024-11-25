package com.kelompok1.jobsphere.ui.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kelompok1.jobsphere.R

@Composable
fun CompanyProfile() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // Header with Background Image and Photo Profile
        HeaderSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Editable Sections
        EditableProfileSection(title = "Description", initialValue = "Lorem Ipsum is simply dummy text...")
        EditableProfileSection(title = "Industrial Sector", initialValue = "Technology")
        EditableProfileSection(title = "Vision and Mission", initialValue = "To innovate and lead the market...")

        Spacer(modifier = Modifier.height(16.dp))
        // Documentation Section
        DocumentationSection()
        Spacer(modifier = Modifier.height(16.dp))
        EditableProfileSection(title = "Vision and Mission", initialValue = "To innovate and lead the market...")
        EditableProfileSection(title = "Vision and Mission", initialValue = "To innovate and lead the market...")


    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Tentukan tinggi untuk header
            .background(Color.LightGray)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.logo), // Ganti dengan gambar latar
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Informasi Perusahaan
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Company's Name",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Address",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }

        // Foto Profil (Ditempatkan di tengah bawah header dengan overlap)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Menempatkan di tengah bawah
                .offset(y = 40.dp) // Geser ke bawah agar keluar dari background image
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Ganti dengan foto profil perusahaan
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(100.dp) // Ukuran foto profil
                    .background(Color.Gray, CircleShape) // Membentuk lingkaran dengan latar abu-abu
                    .padding(4.dp) // Padding di dalam lingkaran
            )
        }
    }
}

@Composable
fun EditableProfileSection(title: String, initialValue: String) {
    val isEditing = remember { mutableStateOf(false) } // State untuk mengontrol mode edit
    val textFieldState = remember { mutableStateOf(initialValue) } // State untuk menyimpan nilai teks

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Bagian judul
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            IconButton(
                onClick = { isEditing.value = !isEditing.value }, // Mengubah mode edit
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24), // Ikon pena (edit)
                    contentDescription = "Edit $title",
                    tint = Color.Blue
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Mode edit atau hanya menampilkan teks
        if (isEditing.value) {
            OutlinedTextField(
                value = textFieldState.value,
                onValueChange = { textFieldState.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Enter $title...",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            )
        } else {
            Text(
                text = textFieldState.value.ifEmpty { "No $title provided" },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun DocumentationSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Documentation",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* TODO: Add documentation action */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(
                text = "Add Documentation",
                color = Color.White
            )
        }
    }
}
