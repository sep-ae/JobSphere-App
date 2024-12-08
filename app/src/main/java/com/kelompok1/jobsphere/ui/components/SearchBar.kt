package com.kelompok1.jobsphere.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kelompok1.jobsphere.R
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(
    role: UserRole,
    jobViewModel: JobViewModel,
    onNavigateToJobDetail: (jobId: String) -> Unit,
    onNavigateToJobView: (jobId: String) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    onNotificationClick: () -> Unit // Added callback for notifications
) {
    var active by remember { mutableStateOf(false) }
    val jobs by jobViewModel.filteredJobs.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange, // Pass the callback to update query externally
            onSearch = { searchQuery ->
                onQueryChange(searchQuery)
                jobViewModel.searchJobs(searchQuery) // Perform final search
            },
            active = active,
            onActiveChange = { isActive -> active = isActive },
            placeholder = {
                Text(text = "Search Jobs")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            },
            trailingIcon = {
                Row {
                    // Show Notification Icon only if the role is not Guest
                    if (role != UserRole.Guest) {
                        IconButton(onClick = onNotificationClick) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                    }
                    // Clear Search Icon
                    if (active || query.isNotEmpty()) {
                        IconButton(onClick = {
                            onQueryChange("")
                            active = false
                            jobViewModel.searchJobs("") // Reset search results
                        }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                        }
                    }
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            if (jobs.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    jobs.forEach { job ->
                        Text(
                            text = job.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    if (role == UserRole.JobSeeker) {
                                        onNavigateToJobDetail(job.id)
                                    } else if (role == UserRole.Company) {
                                        onNavigateToJobView(job.id)
                                    }
                                    active = false
                                }
                        )
                    }
                }
            } else {
                Text(
                    text = "No jobs found",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.Gray
                )
            }
        }
    }
}
