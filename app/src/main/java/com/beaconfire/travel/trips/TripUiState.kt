package com.beaconfire.travel.trips

import com.beaconfire.travel.repo.model.Trip

sealed interface TripUiState {
    data object None : TripUiState
    data object Loading : TripUiState
    data object LoadFailed : TripUiState
    data class LoadSucceed(val trips: List<Trip>) : TripUiState
}