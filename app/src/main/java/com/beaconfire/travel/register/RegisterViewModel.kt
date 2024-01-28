package com.beaconfire.travel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerUiModel = MutableStateFlow(RegisterUiModel(RegisterStatus.None))
    private val _errorMessage = MutableSharedFlow<String?>()

    val registerUiModel: StateFlow<RegisterUiModel> = _registerUiModel
    val errorMessage: SharedFlow<String?> = _errorMessage

    fun register(
        email: String,
        username: String,
        password: String
    ) {
        if (listOf(email, username, password).any { it.isEmpty() }) {
            _registerUiModel.value = RegisterUiModel(RegisterStatus.EmailOrUsernameOrPasswordIsEmpty)
            viewModelScope.launch { _errorMessage.emit("Email, username, and password cannot be empty!") }
            return
        }

        val newUser = User(displayName = username, email = email, password = password)
        _registerUiModel.value = RegisterUiModel(RegisterStatus.Registering)

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.register(newUser) { success ->
                if (success) {
                    _registerUiModel.value = RegisterUiModel(RegisterStatus.RegistrationSuccess)
                } else {
                    _registerUiModel.value = RegisterUiModel(RegisterStatus.RegistrationFailed)
                    launch(Dispatchers.Main) {
                        _errorMessage.emit("Failed to register. Please try again!")
                    }
                }
            }
        }
    }

    companion object Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                // Replace with actual dependency injection or instance creation logic
                val userRepository = UserRepository()
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

// Rest of the classes (RegisterStatus, RegisterUiModel) remain the same
