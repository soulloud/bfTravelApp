package com.beaconfire.travel.search

import com.beaconfire.travel.repo.model.Destination

sealed interface SearchUiModel {
    data object None : SearchUiModel
    data object Searching : SearchUiModel
    data object SearchFailed : SearchUiModel
    data class SearchSucceed(val destinations: List<Destination>) : SearchUiModel
}
