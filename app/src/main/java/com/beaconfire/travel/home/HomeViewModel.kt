package com.beaconfire.travel.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.HomeRepository
import com.beaconfire.travel.repo.model.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
): ViewModel() {

    //first state whether the search is happening or not
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _destination = MutableStateFlow(Destination())
    val destination = _destination.asStateFlow()

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
            homeRepository.searchDestination(
                searchText = searchText.value,
                completion = {destination ->
                    if (destination != null) {
                        _destination.value = destination
                    }
                }
            )
            //Log.d("test", destination.toString())
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