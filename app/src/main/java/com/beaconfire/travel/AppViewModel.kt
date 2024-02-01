package com.beaconfire.travel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppViewModel(
    userRepository: UserRepository
) : ViewModel() {
    private val _currentScreen = MutableStateFlow<Navigation>(Navigation.Splash)
    val appUiModel = userRepository.loginUser.combine(_currentScreen) { user, currentScreen ->
        user?.let { _currentScreen.update { Navigation.Login } }
        AppUiModel(user = user, currentScreen = currentScreen)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AppUiModel(null, Navigation.Splash)
    )

    fun navigateTo(navigation: Navigation) {
        when (navigation) {
            Navigation.Splash -> {
                _currentScreen.update { Navigation.Splash }
            }

            Navigation.Login -> {
                _currentScreen.update { Navigation.Login }
            }

            Navigation.Register -> {
                _currentScreen.update { Navigation.Register }
            }

            else -> {}
        }
    }

    companion object {
        private val TAG = AppViewModel::class.java.simpleName
        val Factory = viewModelFactory {
            initializer {
                AppViewModel(mallApplication().container.userRepository)
            }
        }
    }
}