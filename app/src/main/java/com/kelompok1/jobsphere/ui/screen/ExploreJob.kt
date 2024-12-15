package com.kelompok1.jobsphere.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.UserRole
import com.kelompok1.jobsphere.ui.components.LazyRowCategory
import com.kelompok1.jobsphere.ui.components.SearchBarComponent

@Composable
fun ExploreJob(
    navController: NavController,
    jobViewModel: JobViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    val jobsState by jobViewModel.jobs.collectAsState()
    val filteredJobsState by jobViewModel.filteredJobs.collectAsState()

    val jobs = if (searchQuery.isNotEmpty()) filteredJobsState else jobsState

    if (jobs.isEmpty() && searchQuery.isEmpty()) {
        jobViewModel.fetchJobs(UserRole.Guest)
    }

    Scaffold(
        // Removed FloatingActionButton
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            SearchBarComponent(
                role = UserRole.Guest,
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
                navController.navigate("jobcategoryguest/$category")
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
                LazyColumnGuest(
                    context = LocalContext.current,
                    jobs = jobs,
                    navController = navController
                )
            }
        }
    }
}
