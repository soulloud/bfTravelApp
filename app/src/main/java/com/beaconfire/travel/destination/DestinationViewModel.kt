package com.beaconfire.travel.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.DestinationRepository
import com.beaconfire.travel.repo.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DestinationViewModel(
    private val destinationRepository: DestinationRepository
): ViewModel(){

    private val _tripList = MutableStateFlow<List<Trip>>(emptyList())
    val tripList: StateFlow<List<Trip>> = _tripList.asStateFlow()

    init {
        getAllTrips()
    }

    private fun getAllTrips(){
        viewModelScope.launch {
            //_tripList.value = destinationRepository.getAllTrips("zhao")
            //Log.d("test", tripList.toString())

        }
    }

    companion object {
        private val TAG = DestinationViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                DestinationViewModel(mallApplication().container.destinationRepository)
            }
        }
    }
}