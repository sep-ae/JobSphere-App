package com.kelompok1.jobsphere.ui.company

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.ResultState
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddJobPage(
    navController: NavController,
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel,
    onJobAdded: () -> Unit,
    onError: (String) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf(TextFieldValue("")) }
    var salary by remember { mutableStateOf(TextFieldValue("")) }
    var qualifications by remember { mutableStateOf(TextFieldValue("")) }
    var jobType by remember { mutableStateOf("Full-Time") }
    var deadline by remember { mutableStateOf("") }
    var started by remember { mutableStateOf("") }

    val jobTypes = listOf("Full-Time", "Part-Time", "Freelance", "Internship")
    var expanded by remember { mutableStateOf(false) }

    val jobState by jobViewModel.jobState.collectAsState()

    LaunchedEffect(jobState) {
        when (jobState) {
            is ResultState.Success -> {
                onJobAdded() // Handle successful job addition
                navController.navigate(Screen.CompanyHomePage.route) {
                    // Pop the back stack to avoid going back to the AddJobPage
                    popUpTo(Screen.CompanyHomePage.route) { inclusive = true }
                }
            }
            is ResultState.Failure -> {
                onError((jobState as ResultState.Failure).error)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Add Job Vacancy",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Job Description") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = salary,
            onValueChange = { salary = it },
            label = { Text("Salary") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = qualifications,
            onValueChange = { qualifications = it },
            label = { Text("Qualifications (comma separated)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            OutlinedTextField(
                value = jobType,
                onValueChange = {},
                label = { Text("Job Type") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                jobTypes.forEach { type ->
                    DropdownMenuItem(onClick = {
                        jobType = type
                        expanded = false
                    }) {
                        Text(text = type)
                    }
                }
            }
        }

        OutlinedTextField(
            value = started,
            onValueChange = { started = it },
            label = { Text("Start (yyyy-MM-dd)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = deadline,
            onValueChange = { deadline = it },
            label = { Text("Deadline (yyyy-MM-dd)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    navController.popBackStack() // Go back to the previous screen
                },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(text = "Cancel")
            }

            Button(
                onClick = {
                    if (title.text.isNotBlank() && description.text.isNotBlank() && location.text.isNotBlank() && salary.text.isNotBlank() && qualifications.text.isNotBlank() && deadline.isNotBlank() && started.isNotBlank()) {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val deadlineDate = sdf.parse(deadline)
                        val startedDate = sdf.parse(started)
                        val deadlineTimestamp = deadlineDate?.time ?: System.currentTimeMillis()
                        val startedTimestamp = startedDate?.time ?: System.currentTimeMillis()

                        jobViewModel.addJob(
                            title = title.text,
                            description = description.text,
                            location = location.text,
                            salary = salary.text,
                            qualifications = qualifications.text,
                            jobType = jobType,
                            deadline = deadlineTimestamp,
                            started = startedTimestamp
                        )
                    } else {
                        onError("All fields must be filled!")
                    }
                },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(text = "Add Job")
            }
        }
    }
}
