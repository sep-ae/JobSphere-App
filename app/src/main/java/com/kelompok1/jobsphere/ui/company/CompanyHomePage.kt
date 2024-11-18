package com.kelompok1.jobsphere.ui.company

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ViewModel.UserViewModel
import com.kelompok1.jobsphere.data.model.CustomDrawerState
import com.kelompok1.jobsphere.data.model.NavigationItem
import com.kelompok1.jobsphere.data.model.isOpened
import com.kelompok1.jobsphere.data.model.opposite
import com.kelompok1.jobsphere.ui.components.CustomDrawer
import com.kelompok1.jobsphere.ui.util.coloredShadow
import kotlinx.coroutines.CoroutineScope
import kotlin.math.roundToInt

@Composable
fun CompanyHomePage(
    navController: NavController,
    username: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    userViewModel: UserViewModel
) {
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.Home) }
    var selectedItem by remember { mutableStateOf(0) }

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
            selectedNavigationItem = selectedNavigationItem,
            onNavigationItemClick = {
                selectedNavigationItem = it
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
            onSelectedItem = { index -> selectedItem = index }
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    username: String,
    navController: NavController,
    selectedItem: Int,
    onSelectedItem: (index: Int) -> Unit
) {
    val items = listOf("menu1", "menu2", "menu3", "menu4")

    Scaffold(
        modifier = modifier
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                onDrawerClick(CustomDrawerState.Closed)
            },
        topBar = {
            TopAppBar(
                title = { Text(text = "Welcome $username") },
                navigationIcon = {
                    IconButton(onClick = { onDrawerClick(drawerState.opposite()) }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Icon"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(50)),
                elevation = 10.dp,
                backgroundColor = Color.DarkGray
            ) {
                items.forEachIndexed { index, _ ->
                    BottomNavigationItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Filled.FormatListBulleted, contentDescription = "Menu 1")
                                1 -> Icon(Icons.Filled.ShoppingBag, contentDescription = "Menu 2")
                                2 -> Icon(Icons.Filled.Person, contentDescription = "Menu 3")
                                3 -> Icon(Icons.Filled.Home, contentDescription = "Menu 4")
                            }
                        },
                        selected = selectedItem == index,
                        onClick = {
                            onSelectedItem(index)
                            when (index) {
                                0 -> navController.navigate("Menu1Screen")
                                1 -> navController.navigate("Menu2Screen")
                                2 -> navController.navigate("Menu3Screen")
                                3 -> navController.navigate("Menu4Screen")
                            }
                        },
                        selectedContentColor = Color.Cyan,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Welcome to the Company Home Page",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )

            FloatingActionButton(
                onClick = { /* Action when clicked, e.g., navigate to another screen */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(0.dp)
                    .offset(y = (-90).dp)
                    .padding(end = 24.dp),
                shape = RoundedCornerShape(50),
                containerColor = Color.Blue
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Icon",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun JobItemPlaceholder() {
    Box(
        modifier = Modifier
            .size(100.dp, 80.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Job", color = Color.Gray)
    }
}