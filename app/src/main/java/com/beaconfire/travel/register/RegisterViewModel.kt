package com.beaconfire.travel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.model.City
import com.beaconfire.travel.repo.model.State
import com.beaconfire.travel.repo.model.User
import com.beaconfire.travel.repo.region.RegionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        state: State,
        city: City,
    ) {
        if (listOf(email, username, password, state.state, city.city).any { it.isEmpty() }) {
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

    suspend fun getAllCitiesForStates(state: State) {
        _registerUiModel.update {
            it.copy(registerStatus = RegisterStatus.LoadingCities)
        }
        _registerUiModel.update {
            it.copy(
                registerStatus = RegisterStatus.None,
                cities = regionRepository.getCities(state)
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
                states = regionRepository.getStates(),
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

// Rest of the classes (RegisterStatus, RegisterUiModel) remain the same
