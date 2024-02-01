package com.beaconfire.travel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.mallApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val appContainer: AppContainer
) : ViewModel() {
    private val _settingsUiModel = MutableStateFlow(SettingsUiModel())
    val settingsUiModel: StateFlow<SettingsUiModel> = _settingsUiModel

    init {
        loadUser()
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appContainer.userRepository.logout()
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _settingsUiModel.update {
                    it.copy(
                        user = appContainer.userRepository.getLoginUser()
                    )
                }
            }
        }
    }

    companion object {
        private val TAG = SettingsViewModel::class.java.simpleName
        val Factory = viewModelFactory {
            initializer {
                SettingsViewModel(mallApplication().container)
            }
        }
    }
}