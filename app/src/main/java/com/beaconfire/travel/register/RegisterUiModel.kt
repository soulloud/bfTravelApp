package com.beaconfire.travel.register

enum class RegisterStatus {
    Registering,
    RegistrationFailed,
    RegistrationSuccess,
    EmailOrUsernameOrPasswordIsEmpty,
    None,
}

data class RegisterUiModel(val registerStatus: RegisterStatus)
