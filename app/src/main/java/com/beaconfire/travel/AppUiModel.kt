package com.beaconfire.travel

import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.model.User

data class AppUiModel(val user: User? = null, val currentScreen: Navigation)
