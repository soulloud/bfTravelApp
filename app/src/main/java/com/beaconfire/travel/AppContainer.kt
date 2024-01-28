package com.beaconfire.travel

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.region.ApiClient
import com.beaconfire.travel.repo.region.RegionRepository

interface AppContainer {
    val userRepository: UserRepository
    val regionRepository: RegionRepository
}

class MallAppDataContainer(
    context: Context
) : AppContainer {
    override val userRepository by lazy { UserRepository() }
    override val regionRepository by lazy { RegionRepository(ApiClient) }
}

fun CreationExtras.mallApplication(): TravelApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TravelApplication)
