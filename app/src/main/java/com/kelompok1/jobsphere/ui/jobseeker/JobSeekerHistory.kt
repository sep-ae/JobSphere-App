package com.kelompok1.jobsphere.ui.jobseeker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok1.jobsphere.R // Ganti dengan resource logo Anda

@Composable
fun JobSeekerHistory() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D1D1D)) // Background untuk keseluruhan layar
    ) {
        // Header
        HeaderSection()

        // List of History Items
        HistoryItemList()
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF005B96)) // Ganti dengan warna header Anda
            .padding(16.dp)
    ) {
        Text(
            text = "History",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun HistoryItemList() {
    Column(modifier = Modifier.padding(16.dp)) {
        repeat(5) { // Simulasikan daftar item history
            HistoryItem(
                companyName = "Digital Marketing",
                jobType = "Full-Time",
                description = "Short Description",
                date = "21 Oct"
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HistoryItem(
    companyName: String,
    jobType: String,
    description: String,
    date: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Company Logo Placeholder
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo), // Ganti dengan logo perusahaan Anda
                    contentDescription = "Company Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Text Information
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = companyName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = jobType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right Section (Icon & Date)
            Column(horizontalAlignment = Alignment.End) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Status Icon",
                    tint = Color.Green,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
