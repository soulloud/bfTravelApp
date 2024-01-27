package com.beaconfire.travel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.model.User
import com.beaconfire.travel.repo.region.RegionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val regionRepository: RegionRepository
) : ViewModel() {

    private val _registerUiModel = MutableStateFlow(RegisterUiModel())
    private val _errorMessage = MutableSharedFlow<String?>()

    val registerUiModel: StateFlow<RegisterUiModel> = _registerUiModel
    val errorMessage: SharedFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            getAllStates()
        }
    }

    fun register(
        email: String,
        username: String,
        password: String,
        state: String,
        city: String,
    ) {
        if (listOf(email, username, password, state, city).any { it.isEmpty() }) {
            _registerUiModel.update { it.copy(registerStatus = RegisterStatus.FieldsCannotBeEmpty) }
            viewModelScope.launch { _errorMessage.emit("Email, username, password, state or city cannot be empty!") }
            return
        }
        _registerUiModel.update {
            it.copy(registerStatus = RegisterStatus.Registering)
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val registeredUser = userRepository.register(email, username, password)
                if (User.INVALID_USER == registeredUser) {
                    _errorMessage.emit("Email or username already exist, please try again!")
                }
                _registerUiModel.update {
                    it.copy(registerStatus = if (User.INVALID_USER == registeredUser) RegisterStatus.RegistrationFailed else RegisterStatus.RegistrationSuccess)
                }
            }
        }
    }

    suspend fun getAllCitiesForStates(city: String) {
        _registerUiModel.update {
            it.copy(registerStatus = RegisterStatus.LoadingCities)
        }
        _registerUiModel.update {
            it.copy(
                registerStatus = RegisterStatus.None,
                cities = regionRepository.getAllCitiesForStatus(city)
            )
        }
    }

    private suspend fun getAllStates() {
        _registerUiModel.update {
            it.copy(registerStatus = RegisterStatus.LoadingStates)
        }
        _registerUiModel.update {
            it.copy(
                registerStatus = RegisterStatus.None,
                states = regionRepository.getAllStates(),
                cities = emptyList()
            )
        }
    }

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                RegisterViewModel(
                    mallApplication().container.userRepository,
                    mallApplication().container.regionRepository,
                )
            }
        }
    }
}
