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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val destinationRepository: DestinationRepository
) : ViewModel() {
    private val _searchKeyword = MutableSharedFlow<String>(1)

    private val _searchUiModel = MutableStateFlow(SearchUiModel())
    var searchUiModel: StateFlow<SearchUiModel> = _searchUiModel

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _searchKeyword.debounce(1000).collect { searchKeyword ->
                    _searchUiModel.update {
                        it.copy(
                            searchUiState = SearchUiState.Searching,
                            filter = DestinationFilter.FilterByNameOrLocation(searchKeyword)
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
            }
        }
    }

    fun search(keyword: String) {
        _searchKeyword.tryEmit(keyword)
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
