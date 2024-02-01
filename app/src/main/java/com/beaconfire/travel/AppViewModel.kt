package com.beaconfire.travel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.navigation.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _appUiModel = MutableStateFlow(AppUiModel(Navigation.Splash))
    val appUiModel: StateFlow<AppUiModel> = _appUiModel

    fun navigateTo(navigation: Navigation) {
        when (navigation) {
            Navigation.Splash -> {
                _appUiModel.update { it.copy(currentScreen = Navigation.Splash) }
            }

            Navigation.Login -> {
                _appUiModel.update { it.copy(currentScreen = Navigation.Login) }
            }

            Navigation.Register -> {
                _appUiModel.update { it.copy(currentScreen = Navigation.Register) }
            }

            else -> {}
        }
    }

    companion object {
        private val TAG = AppViewModel::class.java.simpleName
        val Factory = viewModelFactory {
            initializer {
                AppViewModel()
            }
        }
    }
}