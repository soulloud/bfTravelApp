package com.beaconfire.travel.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.HomeRepository
import com.beaconfire.travel.repo.model.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface DestinationUiState {
    data class Success(val destinationList: List<Destination>): DestinationUiState
    data object Error: DestinationUiState
    data object Loading: DestinationUiState
}

class HomeViewModel(
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _destination = MutableStateFlow(Destination())
    val destination = _destination.asStateFlow()

    var destinationUiState: DestinationUiState by mutableStateOf(DestinationUiState.Loading)

    init {
        getDestinationList()
        sortList()
    }

    private fun getDestinationList(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                destinationUiState = try {
                    val result = homeRepository.getDestinations()
                    DestinationUiState.Success(result)
                } catch (e: Exception){
                    DestinationUiState.Error
                }
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    fun onSearch() {
        _isSearching.value = !_isSearching.value
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                destinationUiState = try {
                    val searchResult = homeRepository.searchDestination(searchText.value)
                    DestinationUiState.Success(searchResult)
                } catch (e: Exception) {
                    DestinationUiState.Error
                }
            }
        }
        onSearchTextChange("")
    }

//    fun getImage(): ImageBitmap {
//        viewModelScope.launch {
//
//        }
//    }

    private fun sortList(byAscending: Boolean = false){

        // TODO: Need ratings for sorting
        val comparator: Comparator<Destination> = if (byAscending) {
            compareBy { it.name }
        } else {
            compareByDescending { it.name }
        }
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                HomeViewModel(mallApplication().container.homeRepository)
            }
        }
    }
}