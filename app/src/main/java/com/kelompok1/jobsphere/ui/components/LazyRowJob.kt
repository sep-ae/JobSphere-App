package com.kelompok1.jobsphere.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp


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
                    .padding(8.dp),
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

@Composable
fun LazyRowCategory(onCategoryClick: (String) -> Unit) {
    val categories = remember {
        listOf(
            "Data & Product Management", "Business Dev & Sales",
            "Design & Creative", "Marketing & Social Media",
            "Finance & Accounting", "IT & Engineering",
            "Health & Science", "Recruiting & People"
        )
    }

    val icons = listOf(
        "\uD83D\uDCBB", "\uD83D\uDC65", "\uD83D\uDCDD", "\uD83D\uDCE0",
        "\uD83D\uDCB8", "\uD83D\uDCBB", "\uD83C\uDF77", "\uD83D\uDC68"
    )

    val colors = listOf(
        Color(0xFF1E88E5), Color(0xFF8E24AA), Color(0xFF7B1FA2), Color(0xFF43A047),
        Color(0xFFFFB300), Color(0xFF0288D1), Color(0xFF5E35B1), Color(0xFF388E3C)
    )

    // Get screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = (screenWidth - 32.dp) / 4

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories.chunked(4)) { rowCategories ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowCategories.forEachIndexed { index, category ->
                    CategoryCard(
                        category = category,
                        icon = icons[index],
                        onClick = { onCategoryClick(category) },
                        color = colors[(index + 1) % colors.size],
                        cardWidth = cardWidth
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CategoryCard(category: String, onClick: () -> Unit, color: Color, cardWidth: Dp, icon: String) {
    Card(
        modifier = Modifier
            .width(cardWidth)
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = category,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(4.dp)
                        .size(24.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        style = TextStyle(fontSize = 16.sp, color = color)
                    )
                }
            }
        }
    }
}
