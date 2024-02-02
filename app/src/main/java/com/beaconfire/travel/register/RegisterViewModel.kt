package com.beaconfire.travel.register

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.R
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.model.City
import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.repo.model.State
import com.beaconfire.travel.repo.region.RegionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun register(
        email: String,
        displayName: String,
        password: String,
        state: State,
        city: City
    ) {
        if (listOf(email, displayName, password, state.state, city.city).any { it.isEmpty() }) {
            _registerUiModel.update { it.copy(registerStatus = RegisterStatus.FieldsCannotBeEmpty) }
            viewModelScope.launch { _errorMessage.emit("Email, username, password, state, or city cannot be empty!") }
            return
        }

        _registerUiModel.update {
            it.copy(registerStatus = RegisterStatus.Registering)
        }

        val location = "${city.city}, ${state.state}"
        val profile = Profile(
            fullName = displayName, // Assuming full name is same as username for now
            location = location,
            joinDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = userRepository.register(email, displayName, password, profile)
                if (user == null) {
                    _errorMessage.emit("Email or username already exist, please try again!")
                }
                _registerUiModel.update { it.copy(registerStatus = RegisterStatus.RegistrationSuccess) }
                delay(1000)
                _registerUiModel.update { it.copy(registerStatus = RegisterStatus.None) }
            }
        }
    }

    suspend fun getAllCitiesForStates(state: State) {
        _registerUiModel.update { it.copy(registerStatus = RegisterStatus.LoadingCities) }
        _registerUiModel.update {
            it.copy(
                registerStatus = RegisterStatus.None,
                cities = regionRepository.getCities(state)
            )
        }
    }

    private suspend fun getAllStates() {
        _registerUiModel.update { it.copy(registerStatus = RegisterStatus.LoadingStates) }
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
