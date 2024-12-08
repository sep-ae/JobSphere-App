package com.kelompok1.jobsphere.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.UserRole
import com.kelompok1.jobsphere.ui.components.LazyColumnAllJob
import com.kelompok1.jobsphere.ui.components.LazyRowCategory
import com.kelompok1.jobsphere.ui.components.SearchBarComponent
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Person

@Composable
fun ExploreJob(
    navController: NavController,
    jobViewModel: JobViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    val jobsState by jobViewModel.jobs.collectAsState()
    val filteredJobsState by jobViewModel.filteredJobs.collectAsState()

    val jobs = if (searchQuery.isNotEmpty()) filteredJobsState else jobsState

    // Fetch jobs jika belum ada data
    if (jobs.isEmpty() && searchQuery.isEmpty()) {
        jobViewModel.fetchJobs(UserRole.Guest)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("GuestPage")
                },
                backgroundColor = Color(0xFF134B70),
                contentColor = Color.White,
                shape = RoundedCornerShape(50)
            ) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "Profile")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            SearchBarComponent(
                role = UserRole.Guest, // You can modify this role if needed
                jobViewModel = jobViewModel,
                onNavigateToJobDetail = { jobId -> navController.navigate("JobDetailView/$jobId") },
                onNavigateToJobView = { /* Not needed for JobSeeker */ },
                query = searchQuery,
                onQueryChange = { query ->
                    searchQuery = query
                    jobViewModel.searchJobs(query)
                },
                onNotificationClick = { /* Handle notification action */ }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Job Categories
            LazyRowCategory { category ->
                navController.navigate("job_category/$category")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Job List Header
            Text(
                text = "All Jobs",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Job List
            if (jobs.isEmpty()) {
                Text("No jobs available", modifier = Modifier.padding(16.dp))
                Log.d("ExploreJob", "No jobs available")
            } else {
                LazyColumnAllJob(
                    context = LocalContext.current,
                    jobs = jobs,
                    navController = navController
                )
            }
        }
    }
}
