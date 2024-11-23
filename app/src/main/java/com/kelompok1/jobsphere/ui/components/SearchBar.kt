package com.kelompok1.jobsphere.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kelompok1.jobsphere.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent() {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    // Row containing the SearchBar and the bell icon
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically // Ensure vertical alignment
    ) {
        // Search Bar with left padding
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { newQuery ->
                println("Performing search on query: $newQuery")
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = {
                Text(text = "Search Jobs")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            },
            trailingIcon = {
                Row {
                    IconButton(onClick = { /* Implement mic action here */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = "Mic"
                        )
                    }
                    // Conditional rendering of the Close icon
                    if (active || query.isNotEmpty()) {
                        IconButton(onClick = { query = ""; active = false }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                        }
                    }
                }
            },
            modifier = Modifier
                .weight(1f) // Allow SearchBar to take available space
                .padding(start = 16.dp) // Add left padding
        ) {
            // Add content here if needed
        }

        // Conditional rendering of the Bell Icon
        if (!active) {
            IconButton(onClick = { /* Implement bell action here */ }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(32.dp) // Adjust size as needed
                )
            }
        }
    }
}
