package com.beaconfire.travel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.beaconfire.travel.constant.Constant
import com.beaconfire.travel.repo.DestinationRepository
import com.beaconfire.travel.repo.ProfileRepository
import com.beaconfire.travel.repo.TripRepository
import com.beaconfire.travel.repo.UserRepository
import com.beaconfire.travel.repo.database.RegionDatabase
import com.beaconfire.travel.repo.database.region.RegionDao
import com.beaconfire.travel.repo.region.ApiClient
import com.beaconfire.travel.repo.region.RegionApi
import com.beaconfire.travel.repo.region.RegionRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

interface AppContainer {
    val destinationRepository: DestinationRepository
    val firebaseStore: FirebaseFirestore
    val firebaseStorage: FirebaseStorage
    val profileRepository: ProfileRepository
    val regionApi: RegionApi
    val regionDao: RegionDao
    val regionRepository: RegionRepository
    val userRepository: UserRepository
    val userSharedPreferences: SharedPreferences
    val tripRepository: TripRepository
}

class MallAppDataContainer(
    context: Context
) : AppContainer {
    override val destinationRepository by lazy { DestinationRepository() }
    override val firebaseStore by lazy { FirebaseFirestore.getInstance() }
    override val firebaseStorage by lazy { FirebaseStorage.getInstance() }
    override val profileRepository by lazy { ProfileRepository(this) }
    override val regionApi by lazy { ApiClient.getRegionApi() }
    override val regionDao by lazy { RegionDatabase.getDatabase(context).regionDao() }
    override val regionRepository by lazy { RegionRepository(regionApi, regionDao) }
    override val userRepository by lazy { UserRepository(this) }
    override val userSharedPreferences by lazy {
        context.getSharedPreferences(
            Constant.SP_USER,
            Context.MODE_PRIVATE
        )
    }
    override val tripRepository by lazy { TripRepository(this) }
}

fun CreationExtras.mallApplication(): TravelApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TravelApplication)
