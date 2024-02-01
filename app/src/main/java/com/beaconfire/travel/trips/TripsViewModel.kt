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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripsViewModel(
    private val tripRepository: TripRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    var tripUiState by mutableStateOf<TripUiState>(TripUiState.None)

    //var destinationListUiState by mutableStateOf<DestinationUiState>(DestinationUiState.None)
    var currentDestinationList: List<Destination> = emptyList()
    val totalCost = MutableLiveData(0.0)

    val testReviewRepoOutput = MutableLiveData("")

    init {
        loadTrips()
        //testReviewRepo()
    }

    private fun loadTrips() {
        tripUiState = TripUiState.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tripUiState = try {
                    val trips = tripRepository.getAllTrips()
                    TripUiState.LoadSucceed(trips)
                } catch (e: Exception) {
                    TripUiState.LoadFailed
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

    fun testReviewRepo(){

        viewModelScope.launch {
            //reviewRepository.addNewReview()
        }
    }

    companion object {
        private val TAG = TripsViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                TripsViewModel(
                    mallApplication().container.tripRepository,
                    mallApplication().container.reviewRepository
                )
            }
        }
    }
}