package com.beaconfire.travel.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Navigation(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Login : Navigation(
        route = "login",
        title = "Login",
        icon = Icons.Default.Login
    )

    data object Register : Navigation(
        route = "register",
        title = "Register",
        icon = Icons.Default.AccountBox
    )

    data object Main : Navigation(
        route = "main",
        title = "Main",
        icon = Icons.Default.Home
    )

    data object Home : Navigation(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object Trips : Navigation(
        route = "trips",
        title = "Trips",
        icon = Icons.Default.Flight
    )

    data object Profile : Navigation(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    data object Settings : Navigation(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}
