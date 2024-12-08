package com.kelompok1.jobsphere.ui.jobseeker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kelompok1.jobsphere.ViewModel.ApplicationViewModel
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.data.model.Application
import com.kelompok1.jobsphere.data.model.Job
import com.kelompok1.jobsphere.ui.theme.DarkBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerProgress(
    userId: String,
    applicationViewModel: ApplicationViewModel,
    jobViewModel: JobViewModel,
    navController: NavHostController
) {
    val applications by applicationViewModel.applications.collectAsState()
    val jobs by jobViewModel.jobs.collectAsState()

    // Fetch data when the composable is launched
    LaunchedEffect(Unit) {
        applicationViewModel.fetchApplicationsByUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Application Progress", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBlue)
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                if (applications.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No applications found.",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(applications) { application ->
                            val job = jobs.find { it.id == application.jobId }
                            JobProgressCard(application, job)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun JobProgressCard(application: Application, job: Job?) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Job Title
            Text(
                text = job?.title ?: "Job Title Unknown",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Job Type
            Text(
                text = job?.jobType ?: "Job Type Unknown",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Status Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusRow("Pending", application.status == "Pending", Color.Gray)
                StatusRow("In Progress", application.status == "In Progress", Color.Blue)

                // Accepted or Rejected Row
                val isAccepted = application.status == "Accepted"
                val isRejected = application.status == "Rejected"
                StatusRow(
                    label = if (isAccepted) "Accepted" else if (isRejected) "Rejected" else "Accepted or Rejected",
                    isSelected = isAccepted || isRejected,
                    color = when {
                        isAccepted -> Color.Green
                        isRejected -> Color.Red
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    }
                )
            }
        }
    }
}

@Composable
fun StatusRow(label: String, isSelected: Boolean, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Circle for status
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(MaterialTheme.shapes.small)
                .background(if (isSelected) color else Color.LightGray)
        )

        // Label
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isSelected) color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
