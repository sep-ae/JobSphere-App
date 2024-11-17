package com.kelompok1.jobsphere.ui.navigation

object Screen {
    val Splash = ScreenRoute("splash")
    val Landing = ScreenRoute("landing")
    val Login = ScreenRoute("login")
    val Register = ScreenRoute("register")
    val CompanyHomePage = ScreenRoute("companyHomePage")
}

data class ScreenRoute(val route: String)

