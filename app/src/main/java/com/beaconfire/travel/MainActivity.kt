package com.beaconfire.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beaconfire.travel.login.LoginScreen
import com.beaconfire.travel.login.LoginViewModel
import com.beaconfire.travel.main.MainScreen
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.register.RegisterScreen
import com.beaconfire.travel.register.RegisterViewModel
import com.beaconfire.travel.ui.theme.TravelTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelTheme {
                AppScreen(viewModel(factory = AppViewModel.Factory))
            }
        }
    }
}

@Composable
fun AppScreen(appViewModel: AppViewModel) {
    val appUiModel by appViewModel.appUiModel.collectAsState()
    val loginUser by (LocalContext.current.applicationContext as TravelApplication).container.userRepository.loginUser.collectAsState()

    if (loginUser != null) {
        MainScreen()
    } else {
        when (appUiModel.currentScreen) {
            Navigation.Splash -> {
                SplashScreen()
                LaunchedEffect(null) {
                    delay(3000)
                    appViewModel.navigateTo(Navigation.Login)
                }
            }

            Navigation.Login -> {
                LoginScreen(loginViewModel = viewModel(factory = LoginViewModel.Factory)) {
                    appViewModel.navigateTo(it)
                }
            }

            Navigation.Register -> {
                RegisterScreen(registerViewModel = viewModel(factory = RegisterViewModel.Factory)) {
                    appViewModel.navigateTo(it)
                }
            }

            else -> {}
        }
    }
}

@Composable
fun SplashScreen() {
    Text(text = "SplashScreen")
}
