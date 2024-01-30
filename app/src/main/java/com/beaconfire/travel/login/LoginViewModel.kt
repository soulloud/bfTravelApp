package com.beaconfire.travel.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.model.User
import com.beaconfire.travel.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginUiModel = MutableStateFlow(LoginUiModel(LoginStatus.None, user = null))
    private val _errorMessage = MutableSharedFlow<String?>()

    val loginUiModel: StateFlow<LoginUiModel> = _loginUiModel
    val errorMessage: SharedFlow<String?> = _errorMessage

    init {
        Log.d(TAG, "LoginViewModel()")
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginUiModel.update { it.copy(loginStatus = LoginStatus.UsernameOrPasswordIsEmpty, user = null) }
            viewModelScope.launch { _errorMessage.emit("Email and password cannot be empty!") }
            return
        }
        _loginUiModel.update { it.copy(loginStatus = LoginStatus.Authenticating, user = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val loginUser = userRepository.login(email, password).first()
            if (loginUser == User.INVALID_USER) {
                _errorMessage.emit("Username and password doesn't match, please try again!")
                _loginUiModel.update { it.copy(loginStatus = LoginStatus.AuthenticationFailed, user = null) }
            } else {
                loginUser?.let {
                    it.userId?.let { it1 -> SessionManager.setUserId(it1) }
                }
                _loginUiModel.update {
                    it.copy(loginStatus = if (loginUser == User.INVALID_USER) LoginStatus.AuthenticationFailed else LoginStatus.AuthenticationSuccess, user = loginUser)
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
