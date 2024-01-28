package com.beaconfire.travel.register

import com.beaconfire.travel.repo.model.City
import com.beaconfire.travel.repo.model.State

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
    val states: List<State> = emptyList(),
    val cities: List<City> = emptyList(),
)
