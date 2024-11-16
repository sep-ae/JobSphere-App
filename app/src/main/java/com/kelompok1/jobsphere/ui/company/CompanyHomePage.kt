package com.kelompok1.jobsphere.ui.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.kelompok1.jobsphere.ui.components.BottomNavigationBar
import kotlinx.coroutines.CoroutineScope

@Composable
fun CompanyHomePage(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Section
        Text(
            text = "Hire with Precision",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Appropriate and effective recruitment of the company's best talents",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Field Section
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Find a job") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            trailingIcon = {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Latest Job Openings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { JobItemPlaceholder() }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        Text(text = "Jobs from Last Week", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { JobItemPlaceholder() }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Floating Action Button
        FloatingActionButton(
            onClick = { /* Add Job Action */ },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
            backgroundColor = Color.Black
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Job", tint = Color.White)
        }
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
