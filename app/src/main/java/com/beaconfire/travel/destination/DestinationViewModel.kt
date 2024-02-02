package com.beaconfire.travel.destination

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Review
import com.beaconfire.travel.repo.model.ReviewSort
import com.beaconfire.travel.repo.model.Trip
import com.beaconfire.travel.repo.model.sort
import com.beaconfire.travel.trips.TripUiModel
import com.beaconfire.travel.trips.TripUiState
import com.beaconfire.travel.utils.DestinationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
class DestinationViewModel(
    private val appContainer: AppContainer
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

    fun onSortChanged(sort: ReviewSort) {
        _reviewUiModel.update {
            it.copy(
                reviews = it.reviews.sort(sort),
                sort = sort
            )
        }
    }

    private fun loadTrips() {
        _tripUiModel.update { it.copy(tripUiState = TripUiState.Loading) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allTrips = appContainer.tripRepository.getAllTrips()
                _tripUiModel.update {
                    it.copy(
                        tripUiState = TripUiState.LoadSucceed,
                        trips = allTrips
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNewReview(review: Review) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appContainer.reviewRepository.addNewReview(review)
            }
            val reviews = (_reviewUiModel.value.reviews + review.copy(
                title = appContainer.userRepository.getLoginUser()?.displayName ?: ""
            )).sort(_reviewUiModel.value.sort)
            _reviewUiModel.update {
                it.copy(
                    reviewUiState = ReviewUiState.LoadSucceedByDestination,
                    reviews = reviews
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadReview() {
        _reviewUiModel.update { it.copy(reviewUiState = ReviewUiState.Loading) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val reviews =
                    appContainer.reviewRepository.getAllReviewsOnCurrentDestination(destination)
                _reviewUiModel.update {
                    it.copy(
                        reviewUiState = ReviewUiState.LoadSucceedByDestination,
                        reviews = reviews.sort()
                    )
                }
            }
        }
    }

    fun addToTrip(trip: Trip, destination: Destination) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (!trip.destinations.any { it.destinationId == destination.destinationId }) {
                    if (appContainer.tripRepository.addDestination(trip, destination)) {
                        loadTrips()
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = DestinationViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                DestinationViewModel(mallApplication().container)
            }
        }
    }
}