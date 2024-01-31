package com.beaconfire.travel.home

import com.beaconfire.travel.repo.model.Destination

sealed interface HomeUiModel {
    data object None : HomeUiModel
    data object Loading : HomeUiModel
    data object LoadFailed : HomeUiModel
    data class LoadSucceed(val destinations: List<Destination>) : HomeUiModel
}
