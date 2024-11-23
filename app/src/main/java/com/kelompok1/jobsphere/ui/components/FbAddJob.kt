package com.kelompok1.jobsphere.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.ui.navigation.Screen
import com.kelompok1.jobsphere.ui.theme.DarkBlue

@Composable
fun CustomFloatingActionButton(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    FloatingActionButton(
        onClick = {
            navController.navigate(Screen.AddJobPage.route)
        },
        modifier = modifier
            .padding(end = 24.dp, bottom = 16.dp), // padding agar tidak menempel dengan tepi layar
        shape = RoundedCornerShape(50),
        containerColor = DarkBlue
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Job",
            tint = Color.White,
            modifier = Modifier.size(36.dp)
        )
    }
}
