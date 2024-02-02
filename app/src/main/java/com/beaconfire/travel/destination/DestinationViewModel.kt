package com.beaconfire.travel.destination

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
import com.beaconfire.travel.repo.model.Trip
import com.beaconfire.travel.trips.TripUiModel
import com.beaconfire.travel.trips.TripUiState
import com.beaconfire.travel.utils.DestinationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DestinationViewModel(
    private val destinationRepository: DestinationRepository,
    private val tripRepository: TripRepository,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    private val _tripUiModel = MutableStateFlow(TripUiModel())
    val tripUiModel: StateFlow<TripUiModel> = _tripUiModel

    private val _reviewUiModel = MutableStateFlow(ReviewUiModel())
    val reviewUiModel: StateFlow<ReviewUiModel> = _reviewUiModel

    val destination = DestinationManager.getInstance().destination

    init {
        loadTrips()
        loadReview()
    }

    private fun loadTrips() {
        _tripUiModel.update { it.copy( tripUiState = TripUiState.Loading) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allTrips = tripRepository.getAllTrips()
                _tripUiModel.update {
                    it.copy(
                        tripUiState = TripUiState.LoadSucceed,
                        trips = allTrips
                    )
                }
            }
        }
    }

    private fun loadReview() {
        _reviewUiModel.update { it.copy( reviewUiState = ReviewUiState.Loading) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val reviews = reviewRepository.getAllReviewsOnCurrentDestination(destination)
                _reviewUiModel.update {
                    it.copy(
                        reviewUiState = ReviewUiState.LoadSucceedByDestination,
                        reviews = reviews
                    )
                }
            }
        }
    }

    fun addToTrip(trip: Trip, destination: Destination) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (!trip.destinations.any { it.destinationId == destination.destinationId }) {
                    if (tripRepository.addDestination(trip, destination)) {
                        loadTrips()
                    }
                }
            }
        }
    }

    fun createNewReview(reviewData: ReviewData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
                )
            }
        }
    }
}