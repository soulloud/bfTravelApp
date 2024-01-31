package com.beaconfire.travel.trips

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.TripRepository
import com.beaconfire.travel.repo.data.TripData
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripsViewModel(
    private val tripRepository: TripRepository
): ViewModel() {

    var tripUiState by mutableStateOf<TripUiState>(TripUiState.None)
    //var destinationListUiState by mutableStateOf<DestinationUiState>(DestinationUiState.None)
    var currentDestinationList: List<Destination> = emptyList()
    val totalCost = MutableLiveData(0.0)
    val huichangId = "Aq6oY2SW5NiQRkKwYqPE"

    init {
        loadTrips()
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

    fun setCurrentDestinationList(trip: Trip){
        currentDestinationList = trip.destinations
        getTotalPrice()
    }

    fun removeDestinationFromCurrentTrip(destination: Destination, trip: Trip){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                tripRepository.removeFromCurrentTrip(destination, trip)
            }
            //setCurrentDestinationList(trip)
        }
    }

    fun changeTripVisibility(trip: Trip){
        viewModelScope.launch {
            tripRepository.changeTripVisibility(trip.tripId)
            Log.d(TAG, "visibility changed")
        }
    }

    private fun getTotalPrice(){
        val cost = (currentDestinationList)
            .sumOf { it.price.value }
        totalCost.value = cost
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