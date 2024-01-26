package com.beaconfire.travel.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.beaconfire.travel.login.LoginScreen
import com.beaconfire.travel.login.LoginViewModel
import com.beaconfire.travel.main.MainScreen
import com.beaconfire.travel.register.RegisterScreen
import com.beaconfire.travel.register.RegisterViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: Navigation = Navigation.Login,
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
        composable(route = Navigation.Register.route) {
            RegisterScreen(registerViewModel = viewModel(factory = RegisterViewModel.Factory)) {
                navController.navigate(it.route)
            }
        }
        composable(route = Navigation.Main.route) {
            MainScreen()
        }
    }
}
