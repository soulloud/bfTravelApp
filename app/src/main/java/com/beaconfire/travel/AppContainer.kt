package com.beaconfire.travel

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.beaconfire.travel.repo.DestinationRepository
import com.beaconfire.travel.repo.ProfileRepository
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.database.RegionDatabase
import com.beaconfire.travel.repo.database.region.RegionDao
import com.beaconfire.travel.repo.region.ApiClient
import com.beaconfire.travel.repo.region.RegionApi
import com.beaconfire.travel.repo.region.RegionRepository

interface AppContainer {
    val regionApi: RegionApi
    val regionDao: RegionDao
    val destinationRepository: DestinationRepository
    val regionRepository: RegionRepository
    val userRepository: UserRepository
    val profileRepository: ProfileRepository
}

class MallAppDataContainer(
    context: Context
) : AppContainer {
    override val regionApi by lazy { ApiClient.getRegionApi() }
    override val regionDao by lazy { RegionDatabase.getDatabase(context).regionDao() }
    override val destinationRepository by lazy { DestinationRepository() }
    override val regionRepository by lazy { RegionRepository(regionApi, regionDao) }
    override val userRepository by lazy { UserRepository() }
    override val profileRepository by lazy { ProfileRepository() }
}

fun CreationExtras.mallApplication(): TravelApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TravelApplication)
