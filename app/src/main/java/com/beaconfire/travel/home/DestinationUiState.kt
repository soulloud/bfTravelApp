package com.beaconfire.travel.home

import com.beaconfire.travel.repo.model.Destination

sealed interface HomeUiModel {
    data object None : HomeUiModel
    data object Loading : HomeUiModel
    data object LoadFailed : HomeUiModel
    data class LoadSucceed(val destinations: List<Destination>) : HomeUiModel
}

// Can be reused in the trip view model
sealed interface DestinationUiState {
    data object None : DestinationUiState
    data object Loading : DestinationUiState
    data object LoadFailed : DestinationUiState
    data class LoadSucceed(val destinations: List<Destination>) : DestinationUiState
}