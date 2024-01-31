package com.beaconfire.travel.home

import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.utils.DestinationFilter
import com.beaconfire.travel.utils.DestinationSort

data class HomeUiModel(
    val homeUiState: HomeUiState = HomeUiState.None,
    val destinations: List<Destination> = emptyList(),
    val filter: DestinationFilter = DestinationFilter.FilterByTag(emptyList()),
    val sort: DestinationSort = DestinationSort.None,
)

enum class HomeUiState {
    Loading,
    LoadFailed,
    LoadSucceed,
    None,
}
