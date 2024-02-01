package com.beaconfire.travel.trips

import com.beaconfire.travel.repo.model.Trip

data class TripUiModel (
    val tripUiState: TripUiState = TripUiState.None,
    val trips: List<Trip> = emptyList()
)

enum class TripUiState {
    Loading,
    LoadFailed,
    LoadSucceed,
    None,
}