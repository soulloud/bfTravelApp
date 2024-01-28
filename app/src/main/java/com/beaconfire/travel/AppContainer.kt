package com.beaconfire.travel

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.database.RegionDatabase
import com.beaconfire.travel.repo.database.region.RegionDao
import com.beaconfire.travel.repo.region.ApiClient
import com.beaconfire.travel.repo.region.RegionApi
import com.beaconfire.travel.repo.region.RegionRepository

interface AppContainer {
    val regionApi: RegionApi
    val regionDao: RegionDao
    val userRepository: UserRepository
    val regionRepository: RegionRepository
}

class MallAppDataContainer(
    context: Context
) : AppContainer {
    override val regionApi by lazy { ApiClient.getRegionApi() }
    override val regionDao by lazy { RegionDatabase.getDatabase(context).regionDao() }
    override val userRepository by lazy { UserRepository() }
    override val regionRepository by lazy { RegionRepository(regionApi, regionDao) }
}

fun CreationExtras.mallApplication(): TravelApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TravelApplication)
