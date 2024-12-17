package com.kelompok1.jobsphere.ui.jobseeker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.R
import com.kelompok1.jobsphere.ViewModel.ApplicationViewModel
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.data.model.Application
import com.kelompok1.jobsphere.ui.theme.DarkBlue
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobseekerNotification(
    navController: NavController,
    applicationViewModel: ApplicationViewModel,
    jobViewModel: JobViewModel
) {
    val notifications = applicationViewModel.notifications.collectAsState().value
    val jobs = jobViewModel.jobs.collectAsState().value

    LaunchedEffect(Unit) {
        applicationViewModel.listenForStatusUpdates()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            if (notifications.isEmpty()) {
                Text(text = "No new notifications.")
            } else {
                LazyColumn {
                    items(notifications) { notification ->
                        val jobTitle = jobs.find { it.id == notification.jobId }?.title ?: "Unknown Job"

                        NotificationItem(notification = notification, jobTitle = jobTitle)
                    }
                }
            }
        }

        TopAppBar(
            title = { Text(text = "Notifications", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun NotificationItem(notification: Application, jobTitle: String) {
    val statusColor = when (notification.status) {
        "Accepted" -> Color.Green
        "Rejected" -> Color.Red
        else -> Color.Gray
    }

    val formattedDate = convertTimestampToDate(notification.appliedAt)

    Spacer(modifier = Modifier.height(6.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.megaphone),
                contentDescription = "Notification Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = jobTitle,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Status: ${notification.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Applied at: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

fun convertTimestampToDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
