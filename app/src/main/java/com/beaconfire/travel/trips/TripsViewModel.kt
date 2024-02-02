package com.beaconfire.travel.trips

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.ReviewRepository
import com.beaconfire.travel.repo.TripRepository
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripsViewModel(
    private val tripRepository: TripRepository,
) : ViewModel() {

    private val _tripUiModel = MutableStateFlow(TripUiModel())
    val tripUiModel: StateFlow<TripUiModel> = _tripUiModel
    var currentDestinationList: List<Destination> = emptyList()
    val totalCost = MutableLiveData(0.0)

    init {
        loadTrips()
    }

    private fun loadTrips() {
        _tripUiModel.update { it.copy( tripUiState = TripUiState.Loading) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _tripUiModel.update {
                    it.copy(
                        tripUiState = TripUiState.LoadSucceed,
                        trips = tripRepository.getAllTrips()
                    )
                }
            }
        }
    }

    fun createTrip() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tripRepository.createTrip()
            }
        }
        loadTrips()
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tripRepository.deleteTrip(trip)
            }
        }
        loadTrips()
    }

    fun setCurrentDestinationList(trip: Trip) {
        currentDestinationList = trip.destinations
        getTotalPrice()
    }

    fun addDestination(trip: Trip, destination: Destination) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tripRepository.addDestination(trip, destination)
            }
        }
        loadTrips()
        setCurrentDestinationList(trip)
    }

    fun removeDestination(trip: Trip, destination: Destination) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tripRepository.removeDestination(trip, destination)
            }
        }
        loadTrips()
        setCurrentDestinationList(trip)
    }

    fun toggleTripVisibility(trip: Trip) {
        viewModelScope.launch {
            tripRepository.updateTripVisibility(trip.copy(visibility = trip.visibility.toggle()))
            loadTrips()
        }
    }

    private fun getTotalPrice() {
        val cost = (currentDestinationList)
            .sumOf { it.price.value }
        totalCost.value = cost
    }

    companion object {
        private val TAG = TripsViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                TripsViewModel(
                    mallApplication().container.tripRepository
                )
            }
        }
    }
}