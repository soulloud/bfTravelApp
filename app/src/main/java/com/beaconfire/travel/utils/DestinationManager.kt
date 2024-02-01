package com.beaconfire.travel.utils

import com.beaconfire.travel.repo.model.Destination

class DestinationManager private constructor() {

    var destination: Destination = MockData.destination
        private set

    fun setDestination(destination: Destination){
        this.destination = destination
    }

    fun clear(){
        destination = MockData.destination
    }

    companion object {
        @Volatile
        private var INSTANCE: DestinationManager? = null

        fun getInstance(): DestinationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DestinationManager().also {
                    INSTANCE = it
                }
            }
        }
    }
}