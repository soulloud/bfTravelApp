package com.beaconfire.travel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.mallApplication
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

class HomeViewModel(
    private val appContainer: AppContainer
) : ViewModel() {

    private val _homeUiModel = MutableStateFlow(HomeUiModel())

    val homeUiModel: StateFlow<HomeUiModel> = _homeUiModel

    init {
        loadUser()
        loadDestinations()
    }

    fun onFilterChanged(tags: List<String>) {
        _homeUiModel.update {
            it.copy(
                homeUiState = HomeUiState.Loading,
                filter = DestinationFilter.FilterByTag(tags)
            )
        }
        viewModelScope.launch {
            _homeUiModel.update {
                it.copy(
                    homeUiState = HomeUiState.LoadSucceed,
                    destinations = appContainer.destinationRepository.getAllDestinations()
                        .filterBy(it.filter)
                        .sort(it.sort)
                )
            }
        }
    }

    fun onSortChanged(sort: DestinationSort) {
        _homeUiModel.update {
            it.copy(
                destinations = it.destinations.sort(sort),
                sort = sort
            )
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _homeUiModel.update {
                    it.copy(
                        user = appContainer.userRepository.getLoginUser()
                    )
                }
            }
        }
    }

    private fun loadDestinations() {
        _homeUiModel.update { it.copy(homeUiState = HomeUiState.Loading) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _homeUiModel.update {
                    it.copy(
                        homeUiState = HomeUiState.LoadSucceed,
                        destinations = appContainer.destinationRepository.getAllDestinations()
                            .sort(it.sort)
                    )
                }
            }
        }
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                HomeViewModel(mallApplication().container)
            }
        }
    }
}
