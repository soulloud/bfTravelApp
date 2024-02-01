package com.beaconfire.travel.destination

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.DestinationRepository
import com.beaconfire.travel.repo.ReviewRepository
import com.beaconfire.travel.repo.TripRepository
import com.beaconfire.travel.repo.data.ReviewData
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Review
import com.beaconfire.travel.repo.model.Trip
import com.beaconfire.travel.trips.TripUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DestinationViewModel(
    private val destinationRepository: DestinationRepository,
    private val tripRepository: TripRepository,
    private val reviewRepository: ReviewRepository,
    //private val destination: Destination
) : ViewModel() {

    var tripUiState by mutableStateOf<TripUiState>(TripUiState.None)
    var reviewUiState by mutableStateOf<ReviewUiState>(ReviewUiState.None)


    init {
        loadTrips()
        loadReview()
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

    private fun loadReview(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                reviewUiState = try {
//                    val reviews = reviewRepository
//                        .getAllReviewsOnCurrentDestination(destination)
//                    ReviewUiState.LoadSucceed(reviews)
//                } catch (e: Exception){
//                    ReviewUiState.LoadFailed
//                }
            }
        }
    }

    fun addToTrip(trip: Trip, destination: Destination){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (!trip.destinations.any{ it.destinationId == destination.destinationId}){
                    tripRepository.addDestination(trip, destination)
                }
            }
        }
    }

    fun createNewReview(reviewData: ReviewData){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                reviewRepository.addNewReview(reviewData)
            }
        }
    }

    companion object {
        private val TAG = DestinationViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                DestinationViewModel(
                    mallApplication().container.destinationRepository,
                    mallApplication().container.tripRepository,
                    mallApplication().container.reviewRepository
                    //destination = Destination()
                )
            }
        }
    }
}