package com.kelompok1.jobsphere.ui.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ui.components.BottomNavigationBar
import kotlinx.coroutines.CoroutineScope

@Composable
fun CompanyHomePage(
    username: String,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    userViewModel: UserViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header dengan nama pengguna dan ikon notifikasi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Hire with Precision, $username", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Field Section
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Find a job") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        // Latest Job Openings Section
        Text(
            text = "Recruitments Progress",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(3) {
                JobItemPlaceholder()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        // Jobs from Last Week Section
        Text(
            text = "Jobs from Last Week",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(3) {
                JobItemPlaceholder()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // Bottom Navigation Bar
    BottomNavigationBar(navController = navController, drawerState = drawerState, scope = scope)
}

@Composable
fun JobItemPlaceholder() {
    Box(
        modifier = Modifier
            .size(100.dp, 80.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Job", color = Color.Gray)
    }
}
