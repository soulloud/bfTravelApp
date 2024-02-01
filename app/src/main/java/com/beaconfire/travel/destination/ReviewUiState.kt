package com.beaconfire.travel.destination

import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Review
import com.beaconfire.travel.repo.model.User

data class ReviewUiModel(
    val user: User? = null,
    val destination: Destination? = null,
    val reviewUiState: ReviewUiState = ReviewUiState.None,
    val reviews: List<Review> = emptyList()
)

enum class ReviewUiState {
    Loading,
    LoadFailed,
    LoadSucceedByDestination,
    LoadSucceedByUser,
    None,
}