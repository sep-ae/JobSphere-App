package com.kelompok1.jobsphere.ui.company

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.data.model.Job
import com.kelompok1.jobsphere.ui.components.formatDate
import com.kelompok1.jobsphere.ui.navigation.Screen
import com.kelompok1.jobsphere.ui.theme.DarkBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobHistoryCompany(
    navController: NavController,
    jobs: List<Job>,
    onDeleteJob: (String) -> Unit,
    jobViewModel: JobViewModel
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var jobToDelete by remember { mutableStateOf<Job?>(null) }
    val applicantsCountMap = remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    // Fetch applicants count for each job when the job list is displayed
    LaunchedEffect(jobs) {
        jobs.forEach { job ->
            jobViewModel.getApplicantsCount(job.id).collect { count ->
                applicantsCountMap.value = applicantsCountMap.value.toMutableMap().apply {
                    this[job.id] = count
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Job History", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.CompanyHomePage.route) {
                            popUpTo(Screen.CompanyHomePage.route) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBlue,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddJobPage.route) },
                shape = RoundedCornerShape(50),
                containerColor = DarkBlue
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Icon",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                items(jobs) { job ->
                    val applicantsCount = applicantsCountMap.value[job.id] ?: 0

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate(Screen.JobHistoryCompanyView.createRoute(job.id))
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = job.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Deadline: ${formatDate(job.deadline)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                TextButton(
                                    onClick = {
                                        jobToDelete = job
                                        showDeleteConfirmation = true
                                    },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Delete",
                                        color = DarkBlue,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    // Konfirmasi Hapus
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete the job and all related applications?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        jobToDelete?.let {
                            onDeleteJob(it.id) // Panggil fungsi untuk menghapus pekerjaan
                        }
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("No")
                }
            }
        )
    }
}


fun formatDate(dateMillis: Long): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
    return dateFormat.format(Date(dateMillis))
}
