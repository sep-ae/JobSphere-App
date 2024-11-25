package com.kelompok1.jobsphere.ui.company

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.ResultState
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ui.navigation.Screen
import com.kelompok1.jobsphere.ui.theme.DarkBlue
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJobPage(
    navController: NavController,
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel,
    onJobAdded: () -> Unit,
    onError: (String) -> Unit
) {
    // State untuk form input
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf(TextFieldValue("")) }
    var salary by remember { mutableStateOf(TextFieldValue("")) }
    var qualifications by remember { mutableStateOf(TextFieldValue("")) }
    var jobType by remember { mutableStateOf("Full-Time") }
    var deadline by remember { mutableStateOf("") }
    var started by remember { mutableStateOf("") }

    // State untuk dropdown menu
    val jobTypes = listOf("Full-Time", "Part-Time", "Freelance", "Internship")
    var expanded by remember { mutableStateOf(false) }

    // Observasi state ViewModel
    val jobState by jobViewModel.jobState.collectAsState()

    // Scaffold untuk snackbar dan layout
    val scaffoldState = rememberScaffoldState()

    // Efek untuk merespon perubahan state
    LaunchedEffect(jobState) {
        when (jobState) {
            is ResultState.Success -> {
                onJobAdded() // Callback setelah sukses
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Job added successfully!",
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
                jobViewModel.resetJobState() // Reset state setelah operasi selesai
            }
            is ResultState.Failure -> {
                val errorMessage = (jobState as ResultState.Failure).error
                onError(errorMessage)
                scaffoldState.snackbarHostState.showSnackbar(
                    message = errorMessage,
                    actionLabel = "Retry",
                    duration = SnackbarDuration.Short
                )
                jobViewModel.resetJobState() // Reset state setelah operasi selesai
            }
            else -> {}
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text = "Add Job",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = {
                        navController.navigate(Screen.CompanyHomePage.route) {
                            popUpTo(Screen.CompanyHomePage.route) { inclusive = true }
                        }
                    }) {
                        androidx.compose.material3.Icon(
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Job Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Job Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = salary,
                onValueChange = { salary = it },
                label = { Text("Salary") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = qualifications,
                onValueChange = { qualifications = it },
                label = { Text("Qualifications (comma separated)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Deadline (yyyy-MM-dd)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
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
                        navController.popBackStack(Screen.JobHistoryCompany.route, false)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = DarkBlue,
                        contentColor = Color.White
                    )
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = DarkBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Add Job")
                }
            }
        }
    }
}
