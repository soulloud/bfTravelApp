package com.beaconfire.travel.search

import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.utils.DestinationFilter
import com.beaconfire.travel.utils.DestinationSort

data class SearchUiModel(
    val searchUiState: SearchUiState = SearchUiState.None,
    val destinations: List<Destination> = emptyList(),
    val filter: DestinationFilter = DestinationFilter.FilterByTag(emptyList()),
    val sort: DestinationSort = DestinationSort.None,
)

enum class SearchUiState {
    Searching,
    SearchFailed,
    SearchSucceed,
    None,
}
