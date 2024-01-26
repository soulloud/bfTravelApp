package com.beaconfire.travel.login

import com.beaconfire.travel.repo.model.User

enum class LoginStatus {
    Authenticating,
    AuthenticationFailed,
    AuthenticationSuccess,
    UsernameOrPasswordIsEmpty,
    None,
}

data class LoginUiModel(
    val loginStatus: LoginStatus = LoginStatus.None,
    val user: User = User.INVALID_USER,
)
