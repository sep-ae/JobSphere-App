package com.kelompok1.jobsphere.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok1.jobsphere.data.model.Job
import com.kelompok1.jobsphere.ui.navigation.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CardDefaults


@Composable
fun LazyRowJob(
    context: Context,
    jobs: List<Job>,
    navController: NavController
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(jobs) { job ->
            Card(
                modifier = Modifier
                    .width(250.dp)
                    .height(150.dp)
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(Screen.ProgresView.createRoute(job.id))
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = job.title,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Location: ${job.location}",
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "Type: ${job.jobType}",
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "Salary: $${job.salary}",
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "Pelamar",
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}
