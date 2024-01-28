package com.beaconfire.travel.login

import android.util.Log
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

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginUiModel = MutableStateFlow(LoginUiModel(LoginStatus.None))
    private val _errorMessage = MutableSharedFlow<String?>()

    val loginUiModel: StateFlow<LoginUiModel> = _loginUiModel
    val errorMessage: SharedFlow<String?> = _errorMessage

    init {
        Log.d(TAG, "LoginViewModel()")
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginUiModel.update { it.copy(loginStatus = LoginStatus.UsernameOrPasswordIsEmpty) }
            viewModelScope.launch { _errorMessage.emit("Email and password cannot be empty!") }
            return
        }
        _loginUiModel.update { it.copy(loginStatus = LoginStatus.Authenticating) }
        viewModelScope.launch {
            userRepository.login(email, password) { user ->
                if (user != null) {
                    _loginUiModel.value = LoginUiModel(LoginStatus.AuthenticationSuccess, user)
                } else {
                    _loginUiModel.value = LoginUiModel(LoginStatus.AuthenticationFailed, User.INVALID_USER)
                    launch { // Launch a new coroutine in the viewModelScope
                        _errorMessage.emit("Email and password don't match, please try again!")
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
        val Factory = viewModelFactory {
            initializer {
                LoginViewModel(mallApplication().container.userRepository)
            }
        }
    }
}
