package com.beaconfire.travel.trips

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.home.DestinationUiState
import com.beaconfire.travel.home.HomeUiModel
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.TripRepository
import com.beaconfire.travel.repo.data.TripData
import com.beaconfire.travel.repo.model.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripsViewModel(
    private val tripRepository: TripRepository
): ViewModel() {

    var tripUiState by mutableStateOf<TripUiState>(TripUiState.None)
    var destinationListUiState by mutableStateOf<DestinationUiState>(DestinationUiState.None)
    val huichangId = "Aq6oY2SW5NiQRkKwYqPE"

    init {
        loadTrips()
//        if (tripUiState is TripUiState.LoadSucceed){
//            changeTripVisibility((tripUiState as TripUiState.LoadSucceed).trips[0])
//        }
    }

    private fun loadTrips(){
        tripUiState = TripUiState.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                tripUiState = try {
                    val trips = tripRepository.getAllTrips(huichangId)
                    TripUiState.LoadSucceed(trips)
                } catch (e: Exception){
                    TripUiState.LoadFailed
                }
            }
        }
    }

    fun createNewTrip(tripData: TripData){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                tripRepository.createNewTrip(tripData)
            }
        }
        loadTrips()
    }

    fun deleteCurrentTrip(trip: Trip){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                tripRepository.deleteCurrentTrip(trip.tripId)
            }
        }
        loadTrips()
    }

    fun removeDestinationFromCurrentTrip(){

    }

    fun changeTripVisibility(trip: Trip){
        viewModelScope.launch {
            tripRepository.changeTripVisibility(trip.tripId)
            Log.d(TAG, "visibility changed")
        }
    }

    companion object {
        private val TAG = TripsViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                TripsViewModel(mallApplication().container.tripRepository)
            }
        }
    }
}