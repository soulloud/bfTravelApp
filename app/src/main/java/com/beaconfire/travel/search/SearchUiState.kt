package com.beaconfire.travel.search

import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.DestinationFilter
import com.beaconfire.travel.repo.model.DestinationSort
import com.beaconfire.travel.repo.model.Review

data class SearchUiModel(
    val searchUiState: SearchUiState = SearchUiState.None,
    val destinations: List<Destination> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val filter: DestinationFilter = DestinationFilter.FilterByTag(emptyList()),
    val sort: DestinationSort = DestinationSort.None,
)

enum class SearchUiState {
    Searching,
    SearchFailed,
    SearchSucceed,
    None,
}
