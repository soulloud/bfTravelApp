package com.beaconfire.travel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel(
    userRepository: UserRepository
) : ViewModel() {
    private val _currentScreen = MutableStateFlow<Navigation>(Navigation.Splash)
    private val _currentUser = MutableStateFlow<User?>(null)
    private val _user = merge(userRepository.loginUser, _currentUser)
    val appUiModel = _user.combine(_currentScreen) { user, currentScreen ->
        user?.let { _currentScreen.update { Navigation.Login } }
        AppUiModel(user = user, currentScreen = currentScreen).also { Log.d(TAG, "$it") }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AppUiModel(null, Navigation.Splash)
    )

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _currentUser.update { userRepository.getLoginUser() }
            }
        }
    }

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