package com.beaconfire.travel.register

enum class RegisterStatus {
    Registering,
    RegistrationFailed,
    RegistrationSuccess,
    FieldsCannotBeEmpty,
    LoadingStates,
    LoadingCities,
    None,
}

data class RegisterUiModel(
    val registerStatus: RegisterStatus = RegisterStatus.None,
    val states: List<String> = emptyList(),
    val cities: List<String> = emptyList(),
)
