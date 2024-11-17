package com.kelompok1.jobsphere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(navController: NavController, drawerState: DrawerState, scope: CoroutineScope, modifier: Modifier = Modifier) {
    // Rounded bottom navigation bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp) // Adjust height as needed
            .padding(horizontal = 16.dp) // Add padding to create rounded edges
            .background(
                color = Color(0xFF134074), // Replace with desired background color
                shape = RoundedCornerShape(36.dp) // Fully rounded corners
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItemCustom(
                icon = Icons.Default.Menu,
                label = "",
                isSelected = false,
                onClick = { scope.launch { drawerState.open() } }
            )
            BottomNavigationItemCustom(
                icon = Icons.Default.Work,
                label = "",
                isSelected = navController.currentDestination?.route == "jobs",
                onClick = { navController.navigate("jobs") }
            )
            BottomNavigationItemCustom(
                icon = Icons.Default.Person,
                label = "",
                isSelected = navController.currentDestination?.route == "profile",
                onClick = { navController.navigate("profile") }
            )
            BottomNavigationItemCustom(
                icon = Icons.Default.Home,
                label = "",
                isSelected = navController.currentDestination?.route == "home",
                onClick = { navController.navigate("home") }
            )
        }
    }
}

@Composable
fun BottomNavigationItemCustom(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.White else Color.LightGray
        )
    }
}
