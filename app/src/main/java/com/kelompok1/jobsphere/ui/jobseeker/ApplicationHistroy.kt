package com.kelompok1.jobsphere.ui.jobseeker

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok1.jobsphere.R
import com.kelompok1.jobsphere.ViewModel.ApplicationViewModel
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.UserRole
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.data.model.Application
import com.kelompok1.jobsphere.data.model.Job
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationHistory(
    jobViewModel: JobViewModel,
    applicationViewModel: ApplicationViewModel,
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit
) {
    val applications = applicationViewModel.applications.collectAsState().value

    LaunchedEffect(Unit) {
        applicationViewModel.fetchApplicationsByUser()
        jobViewModel.fetchJobs(UserRole.JobSeeker)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Application History", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1)
                )
            )
        },
        content = { innerPadding ->
            Column(
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
                            text = "You have not applied to any jobs yet.",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(applications) { application ->
                            val job = jobViewModel.jobs.collectAsState().value
                                .find { it.id == application.jobId }
                            ApplicationHistoryCard(application, job, userViewModel)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ApplicationHistoryCard(application: Application, job: Job?, userViewModel: UserViewModel) {
    var username by remember { mutableStateOf("Loading...") }

    LaunchedEffect(job?.userId) {
        if (job?.userId != null) {
            username = userViewModel.fetchUsernameById(job.userId) ?: "Unknown Company"
        } else {
            username = "Unknown Company"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .shadow(8.dp, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
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
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Company Name dengan ikon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.office),
                    contentDescription = "Company Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                )
            }

            // Application Date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Applied Date",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Applied: ${formatTimestamp(application.appliedAt)}",
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    return try {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        "Unknown"
    }
}
