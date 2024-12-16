package com.kelompok1.jobsphere.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.R
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.data.model.Job
import com.kelompok1.jobsphere.ui.navigation.Screen
import com.kelompok1.jobsphere.ui.screen.DropdownFilter
import com.kelompok1.jobsphere.ui.screen.formatCurrency
import com.kelompok1.jobsphere.ui.screen.isSalaryInRange


@Composable
fun LazyColumnAllJob(
    context: Context,
    jobs: List<Job>,
    navController: NavController,
    userViewModel: UserViewModel
) {
    var selectedJobType by remember { mutableStateOf("All") }
    var selectedLocation by remember { mutableStateOf("All") }
    var selectedSalary by remember { mutableStateOf("All") }
    var selectedEducation by remember { mutableStateOf("All") }

    val filteredJobs = jobs.filter {
        (selectedJobType == "All" || it.jobType == selectedJobType) &&
                (selectedLocation == "All" || it.location == selectedLocation) &&
                (selectedSalary == "All" || isSalaryInRange(it.salary, selectedSalary)) &&
                (selectedEducation == "All" || it.minEducation.equals(selectedEducation, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Filter Section
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                DropdownFilter(
                    label = "Job Type",
                    options = listOf("All", "Full-Time", "Part-Time", "Freelance", "Internship"),
                    selectedOption = selectedJobType,
                    onOptionSelected = { selectedJobType = it }
                )
            }

            item {
                DropdownFilter(
                    label = "Location",
                    options = listOf(
                        "All", "Jakarta", "Bandung", "Surabaya", "Yogyakarta", "Medan", "Denpasar"
                    ),
                    selectedOption = selectedLocation,
                    onOptionSelected = { selectedLocation = it }
                )
            }

            item {
                DropdownFilter(
                    label = "Salary",
                    options = listOf("All", "0-3 juta", "3-6 juta", "6-10 juta", ">10 juta"),
                    selectedOption = selectedSalary,
                    onOptionSelected = { selectedSalary = it }
                )
            }

            item {
                DropdownFilter(
                    label = "Education",
                    options = listOf("All", "SD", "SMP", "SMA/SMK", "D3", "S1", "S2", "S3"),
                    selectedOption = selectedEducation,
                    onOptionSelected = { selectedEducation = it }
                )
            }
        }

        // Job List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(filteredJobs) { job ->
                JobCard(job = job, navController = navController, userViewModel = userViewModel)
            }
        }
    }
}

@Composable
fun JobCard(
    job: Job,
    navController: NavController,
    userViewModel: UserViewModel
) {
    var username by remember { mutableStateOf("Loading...") }

    LaunchedEffect(job.userId) {
        username = userViewModel.fetchUsernameById(job.userId) ?: "Unknown Company"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable {
                navController.navigate(Screen.JobDetailView.createRoute(job.id))
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.job_placeholder_background),
                contentDescription = "Job Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp)
            ) {
                // Job title
                Text(
                    text = job.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                // Company username
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.office),
                        contentDescription = "Office Icon",
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = username,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Job Type Icon",
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = job.jobType,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Salary
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.money),
                            contentDescription = "Salary Icon",
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = formatCurrency(job.salary),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Location Icon",
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = job.location,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Education
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.education),
                        contentDescription = "Education Icon",
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = job.minEducation,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
