package com.kelompok1.jobsphere.ui.jobseeker

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.ViewModel.JobViewModel
import com.kelompok1.jobsphere.ViewModel.UserRole
import com.kelompok1.jobsphere.data.model.CustomDrawerState
import com.kelompok1.jobsphere.data.model.Job
import com.kelompok1.jobsphere.data.model.NavigationItem
import com.kelompok1.jobsphere.data.model.isOpened
import com.kelompok1.jobsphere.data.model.opposite
import com.kelompok1.jobsphere.ui.components.CustomDrawer
import com.kelompok1.jobsphere.ui.components.LazyColumnAllJob
import com.kelompok1.jobsphere.ui.components.LazyRowCategory
import com.kelompok1.jobsphere.ui.components.SearchBarComponent
import com.kelompok1.jobsphere.ui.navigation.Screen
import com.kelompok1.jobsphere.ui.util.coloredShadow
import kotlinx.coroutines.CoroutineScope
import kotlin.math.roundToInt

@Composable
fun JobSeekerHomePage(
    navController: NavController,
    username: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    userViewModel: UserViewModel,
    jobViewModel: JobViewModel
) {
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Home) }
    var selectedItem by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp,
        label = "Animated Offset"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
        label = "Animated Scale"
    )

    val jobsState by jobViewModel.jobs.collectAsState()
    val filteredJobsState by jobViewModel.filteredJobs.collectAsState()

    val jobs = if (searchQuery.isNotEmpty()) filteredJobsState else jobsState

    Log.d("JobSeekerHomePage", "Jobs state in UI: $jobsState")

    if (jobs.isEmpty() && searchQuery.isEmpty()) {
        jobViewModel.fetchJobs(UserRole.JobSeeker)
    }

    fun handleLogout(navController: NavController, userViewModel: UserViewModel, jobViewModel: JobViewModel) {
        userViewModel.logoutUser()
        jobViewModel.resetJobState()
        navController.navigate("login") {
            popUpTo(0) { inclusive = true }
        }
    }

    BackHandler(enabled = drawerState.isOpened()) {
        drawerState = CustomDrawerState.Closed
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        CustomDrawer(
            username = username,
            selectedNavigationItem = selectedNavigationItem,
            onNavigationItemClick = { navigationItem ->
                when (navigationItem) {
                    NavigationItem.Home -> navController.navigate(Screen.JobSeekerHomePage.route)
                    NavigationItem.Profile -> navController.navigate(Screen.JobSeekerProfile.route)
                    NavigationItem.Progress -> navController.navigate(Screen.JobSeekerProgress.route)
                    NavigationItem.Settings -> navController.navigate(Screen.SettingsScreen.route)
                    NavigationItem.Premium -> navController.navigate(Screen.SettingsScreen.route)
                    NavigationItem.Logout -> {
                        handleLogout(navController, userViewModel, jobViewModel)
                    }
                }
                drawerState = CustomDrawerState.Closed
            },
            onCloseClick = { drawerState = CustomDrawerState.Closed }
        )
        MainContent(
            modifier = Modifier
                .offset(x = animatedOffset)
                .scale(animatedScale)
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.1f,
                    shadowRadius = 50.dp
                ),
            drawerState = drawerState,
            onDrawerClick = { drawerState = it },
            username = username,
            navController = navController,
            selectedItem = selectedItem,
            onSelectedItem = { index -> selectedItem = index },
            jobs = jobs,
            jobViewModel = jobViewModel,
            searchQuery = searchQuery,
            onSearchQueryChange = { query ->
                searchQuery = query
                jobViewModel.searchJobs(query)
            }
        )
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    username: String,
    navController: NavController,
    selectedItem: Int,
    onSelectedItem: (index: Int) -> Unit,
    jobs: List<Job>,
    jobViewModel: JobViewModel,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    val context = LocalContext.current

    val items = listOf(
        Pair("Menu", Icons.Filled.Menu),
        Pair("History", Icons.Filled.Timelapse),
        Pair("Profile", Icons.Filled.Person)
    )

    Scaffold(
        modifier = modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(50)),
                backgroundColor = Color(0xFF134B70),
                elevation = 10.dp
            ) {
                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = item.second,
                                contentDescription = item.first,
                                tint = Color.White
                            )
                        },
                        selected = selectedItem == index,
                        onClick = {
                            onSelectedItem(index)
                            when (index) {
                                0 -> onDrawerClick(drawerState.opposite())
                                1 -> navController.navigate(Screen.ApplicationHistory.route)
                                2 -> navController.navigate(Screen.JobSeekerProfile.route)
                            }
                        },
                        selectedContentColor = Color.Cyan,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    SearchBarComponent(
                        role = UserRole.JobSeeker,
                        jobViewModel = jobViewModel,
                        onNavigateToJobDetail = { jobId -> navController.navigate("JobDetailView/$jobId") },
                        onNavigateToJobView = { /* Not needed for JobSeeker */ },
                        query = searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onNotificationClick = { navController.navigate(Screen.JobseekerNotification.route) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRowCategory { category ->
                        navController.navigate("job_category/$category")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "All Jobs",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (jobs.isEmpty()) {
                        Text("No jobs available", modifier = Modifier.padding(16.dp))
                        Log.d("MainContent", "No jobs available")
                    } else {
                        Log.d("MainContent", "Displaying ${jobs.size} jobs")
                        LazyColumnAllJob(context = context, jobs = jobs, navController = navController)
                    }
                }
            }
        }
    )
}
