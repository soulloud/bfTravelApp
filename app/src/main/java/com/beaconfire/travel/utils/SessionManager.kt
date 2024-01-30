package com.beaconfire.travel.utils

object SessionManager {
    var userId: String? = null
        private set

    fun setUserId(newUserId: String) {
        userId = newUserId
    }

    fun clearUserId() {
        userId = null
    }
}
