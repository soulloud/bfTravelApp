package com.beaconfire.travel.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.beaconfire.travel.home.HomeScreen
import com.beaconfire.travel.login.LoginScreen
import com.beaconfire.travel.login.LoginViewModel
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.profile.ProfileScreen
import com.beaconfire.travel.settings.SettingsScreen
import com.beaconfire.travel.trips.TripsScreen

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    startDestination: Navigation = Navigation.Home,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        composable(route = Navigation.Login.route) {
            LoginScreen(loginViewModel = viewModel(factory = LoginViewModel.Factory)) {
                navController.navigate(it.route)
            }
        }
        composable(route = Navigation.Home.route) {
            HomeScreen()
        }
        composable(route = Navigation.Trips.route) {
            TripsScreen()
        }
        composable(route = Navigation.Profile.route) {
            ProfileScreen()
        }
        composable(route = Navigation.Settings.route) {
            SettingsScreen()
        }
    }
}
