package com.beaconfire.travel.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.DestinationRepository
import com.beaconfire.travel.utils.SortMethod
import com.beaconfire.travel.utils.sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    var searchUiModel by mutableStateOf<SearchUiModel>(SearchUiModel.None)

    fun search(keyword: String) {
        searchUiModel = SearchUiModel.Searching
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchUiModel = try {
                    val destinations = destinationRepository.searchDestination(keyword)
                        .first()
                        .sort(SortMethod.AlphabetAscending)
                    SearchUiModel.SearchSucceed(destinations)
                } catch (e: Exception) {
                    SearchUiModel.SearchFailed
                }
            }
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
