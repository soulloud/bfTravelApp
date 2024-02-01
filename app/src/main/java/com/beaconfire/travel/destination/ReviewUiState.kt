package com.beaconfire.travel.destination

import com.beaconfire.travel.repo.model.Review

sealed interface ReviewUiState {
    data object None : ReviewUiState
    data object Loading : ReviewUiState
    data object LoadFailed : ReviewUiState
    data class LoadSucceed(val reviews: List<Review>) : ReviewUiState
}
