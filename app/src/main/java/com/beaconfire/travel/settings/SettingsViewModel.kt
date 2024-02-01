package com.beaconfire.travel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.logout()
            }
        }
    }

    companion object {
        private val TAG = SettingsViewModel::class.java.simpleName
        val Factory = viewModelFactory {
            initializer {
                SettingsViewModel(mallApplication().container.userRepository)
            }
        }
    }
}