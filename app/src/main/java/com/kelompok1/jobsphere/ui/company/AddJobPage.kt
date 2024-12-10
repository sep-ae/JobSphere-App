package com.kelompok1.jobsphere.ui.company

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.ResultState
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ui.navigation.Screen
import com.kelompok1.jobsphere.ui.theme.DarkBlue
import com.kelompok1.jobsphere.ui.theme.Grey
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StyledAddJobTextField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(35.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Grey,
                focusedBorderColor = Grey,
                unfocusedBorderColor = Grey
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType)
        )
    }
}

@Composable
fun StyledDropdownButton(
    label: String,
    selectedValue: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        OutlinedButton(
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(35.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Grey)
        ) {
            Text(text = selectedValue, color = Color.Black)
        }
    }
}

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
    var minEducation by remember { mutableStateOf(TextFieldValue("")) }
    var jobType by remember { mutableStateOf("Full-Time") }
    var jobCategory by remember { mutableStateOf("Data & Product Management") }
    var expandedJobType by remember { mutableStateOf(false) }
    var expandedJobCategory by remember { mutableStateOf(false) }

    var selectedStartDate by remember { mutableStateOf<Long?>(null) }
    var selectedEndDate by remember { mutableStateOf<Long?>(null) }
    val showStartDatePickerDialog = remember { mutableStateOf(false) }
    val showEndDatePickerDialog = remember { mutableStateOf(false) }

    val jobTypes = listOf("Full-Time", "Part-Time", "Freelance", "Internship")
    val jobCategories = listOf(
        "Data & Product Management", "Business Dev & Sales", "Design & Creative",
        "Marketing & Social Media", "Finance & Accounting", "IT & Engineering",
        "Health & Science", "Recruiting & People"
    )

    val jobState by jobViewModel.jobState.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(jobState) {
        when (jobState) {
            is ResultState.Success -> {
                onJobAdded()
                // Clear form fields
                title = TextFieldValue("")
                description = TextFieldValue("")
                location = TextFieldValue("")
                salary = TextFieldValue("")
                qualifications = TextFieldValue("")
                minEducation = TextFieldValue("")
                jobType = "Full-Time"
                jobCategory = "Data & Product Management"
                selectedStartDate = null
                selectedEndDate = null
            }
            is ResultState.Failure -> {
                onError((jobState as ResultState.Failure).error)
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Job", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = DarkBlue
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            StyledAddJobTextField(
                label = "Job Title",
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            StyledAddJobTextField(
                label = "Job Description",
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            StyledAddJobTextField(
                label = "Location",
                value = location,
                onValueChange = { location = it },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            StyledAddJobTextField(
                label = "Salary",
                value = salary,
                onValueChange = { salary = it },
                modifier = Modifier.padding(bottom = 16.dp),
                keyboardType = KeyboardType.Number
            )
            StyledAddJobTextField(
                label = "Qualifications (comma separated)",
                value = qualifications,
                onValueChange = { qualifications = it },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            StyledAddJobTextField(
                label = "Minimum Education",
                value = minEducation,
                onValueChange = { minEducation = it },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            StyledDropdownButton(
                label = "Job Type",
                selectedValue = jobType,
                onButtonClick = { expandedJobType = !expandedJobType },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            DropdownMenu(
                expanded = expandedJobType,
                onDismissRequest = { expandedJobType = false }
            ) {
                jobTypes.forEach { type ->
                    DropdownMenuItem(onClick = {
                        jobType = type
                        expandedJobType = false
                    }) {
                        Text(text = type)
                    }
                }
            }

            StyledDropdownButton(
                label = "Job Category",
                selectedValue = jobCategory,
                onButtonClick = { expandedJobCategory = !expandedJobCategory },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            DropdownMenu(
                expanded = expandedJobCategory,
                onDismissRequest = { expandedJobCategory = false }
            ) {
                jobCategories.forEach { category ->
                    DropdownMenuItem(onClick = {
                        jobCategory = category
                        expandedJobCategory = false
                    }) {
                        Text(text = category)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { showStartDatePickerDialog.value = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Grey),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        text = selectedStartDate?.let { SimpleDateFormat("yyyy-MM-dd").format(Date(it)) } ?: "Start Date",
                        color = Color.Black
                    )
                }

                Button(
                    onClick = { showEndDatePickerDialog.value = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Grey),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        text = selectedEndDate?.let { SimpleDateFormat("yyyy-MM-dd").format(Date(it)) } ?: "Deadline",
                        color = Color.Black
                    )
                }
            }

            // DatePickerDialogs
            if (showStartDatePickerDialog.value) {
                DatePickerDialog(
                    context = LocalContext.current,
                    onDateSelected = { selectedStartDate = it },
                    onDismiss = { showStartDatePickerDialog.value = false }
                )
            }

            if (showEndDatePickerDialog.value) {
                DatePickerDialog(
                    context = LocalContext.current,
                    onDateSelected = { selectedEndDate = it },
                    onDismiss = { showEndDatePickerDialog.value = false }
                )
            }

            // Button Row for cancel and add job
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack(Screen.JobHistoryCompany.route, false) },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
                ) {
                    Text(text = "Cancel", color = Color.White)
                }

                Button(
                    onClick = {
                        // Show confirmation dialog before proceeding to add job
                        if (title.text.isNotBlank() && description.text.isNotBlank() &&
                            location.text.isNotBlank() && salary.text.isNotBlank() &&
                            qualifications.text.isNotBlank()
                        ) {
                            showConfirmDialog = true
                        } else {
                            onError("All fields must be filled!")
                        }
                    },
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue)
                ) {
                    Text(text = "Add Job", color = Color.White)
                }
            }

            // Confirmation Dialog
            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Confirm") },
                    text = { Text("Are you sure you want to add this job?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                jobViewModel.addJob(
                                    title = title.text,
                                    description = description.text,
                                    location = location.text,
                                    salary = salary.text,
                                    qualifications = qualifications.text,
                                    minEducation = minEducation.text,
                                    jobType = jobType,
                                    jobCategory = jobCategory,
                                    started = selectedStartDate ?: System.currentTimeMillis(),
                                    deadline = selectedEndDate ?: System.currentTimeMillis()
                                )
                                showConfirmDialog = false
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmDialog = false }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DatePickerDialog(
    context: Context,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            onDateSelected(selectedDate.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePicker.setOnDismissListener { onDismiss() }
    datePicker.show()
}
