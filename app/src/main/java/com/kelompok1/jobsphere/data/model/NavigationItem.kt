package com.kelompok1.jobsphere.data.model

import com.kelompok1.jobsphere.R

enum class NavigationItem(
    val title: String,
    val icon: Int
) {
    Home(
        icon = R.drawable.baseline_home_filled_24,
        title = "Home"
    ),
    Profile(
        icon = R.drawable.baseline_person_24,
        title = "Profile"
    ),
    Premium(
        icon = R.drawable.baseline_person_24,
        title = "History"
    ),
    Settings(
        icon = R.drawable.baseline_setting_24,
        title = "Settings"
    ),
    Logout(
        icon = R.drawable.baseline_logout_24,
        title = "Logout"
    ),
    Progress(
        icon = R.drawable.baseline_access_time_24,
        title = "Progres"
    )
}
