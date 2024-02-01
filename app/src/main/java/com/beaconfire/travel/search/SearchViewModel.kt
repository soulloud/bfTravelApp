package com.beaconfire.travel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.DestinationRepository
import com.beaconfire.travel.utils.DestinationFilter
import com.beaconfire.travel.utils.DestinationSort
import com.beaconfire.travel.utils.filterBy
import com.beaconfire.travel.utils.sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    private val _searchUiModel = MutableStateFlow(SearchUiModel())
    var searchUiModel: StateFlow<SearchUiModel> = _searchUiModel

    fun search(keyword: String) {
        _searchUiModel.update {
            it.copy(
                searchUiState = SearchUiState.Searching,
                filter = DestinationFilter.FilterByLocation(keyword)
            )
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _searchUiModel.update {
                    it.copy(
                        searchUiState = SearchUiState.SearchSucceed,
                        destinations = destinationRepository.getAllDestinations()
                            .filterBy(it.filter)
                            .sort(it.sort)
                    )
                }
            }
        }
    }

    fun onSortChanged(sort: DestinationSort) {
        _searchUiModel.update {
            it.copy(
                destinations = it.destinations.sort(sort),
                sort = sort
            )
        }
    }

    companion object {
        private val TAG = SearchViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                SearchViewModel(mallApplication().container.destinationRepository)
            }
        }
    }
}
