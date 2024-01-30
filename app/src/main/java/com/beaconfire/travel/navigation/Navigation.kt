package com.beaconfire.travel.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Navigation(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Back : Navigation(
        route = "back",
        title = "Back",
        icon = Icons.Default.ArrowBackIosNew
    )

    data object DestinationDetail : Navigation(
        route = "destinationDetail",
        title = "DestinationDetail",
        icon = Icons.Default.Place
    )

    data object Home : Navigation(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object Login : Navigation(
        route = "login",
        title = "Login",
        icon = Icons.Default.Login
    )

    data object Main : Navigation(
        route = "main",
        title = "Main",
        icon = Icons.Default.Home
    )

    data object Profile : Navigation(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    data object Register : Navigation(
        route = "register",
        title = "Register",
        icon = Icons.Default.AccountBox
    )

    data object Search : Navigation(
        route = "search",
        title = "Search",
        icon = Icons.Default.Search
    )

    data object Settings : Navigation(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )

    data object Trips : Navigation(
        route = "trips",
        title = "Trips",
        icon = Icons.Default.Flight
    )
}
