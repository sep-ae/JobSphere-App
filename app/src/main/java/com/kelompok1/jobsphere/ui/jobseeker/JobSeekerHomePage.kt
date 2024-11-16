package com.kelompok1.jobsphere.ui.jobseeker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ui.components.BottomNavigationBar
import kotlinx.coroutines.CoroutineScope

@Composable
fun JobSeekerHomeContent(
    username: String,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
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
                Text(text = "Hi, $username", fontSize = 24.sp, fontWeight = FontWeight.Bold)
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
                text = "Latest Job Openings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(3) {
                    com.kelompok1.jobsphere.ui.company.JobItemPlaceholder()
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
                    com.kelompok1.jobsphere.ui.company.JobItemPlaceholder()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Navigation Bar
        BottomNavigationBar(navController = navController, drawerState = drawerState, scope = scope)
    }




