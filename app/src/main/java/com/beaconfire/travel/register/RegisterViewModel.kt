package com.beaconfire.travel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            _registerUiModel.update { RegisterUiModel(RegisterStatus.EmailOrUsernameOrPasswordIsEmpty) }
            viewModelScope.launch { _errorMessage.emit("Email, username and password cannot be empty!") }
            return
        }
        _registerUiModel.update { RegisterUiModel(RegisterStatus.Registering) }
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

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                RegisterViewModel(mallApplication().container.userRepository)
            }
        }
    }
}
