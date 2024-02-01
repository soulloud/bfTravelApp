package com.beaconfire.travel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.beaconfire.travel.constant.Constant
import com.beaconfire.travel.repo.AssetRepository
import com.beaconfire.travel.repo.DestinationRepository
import com.beaconfire.travel.repo.ProfileRepository
import com.beaconfire.travel.repo.ReviewRepository
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
    val assetRepository: AssetRepository
    val context: Context
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
    val reviewRepository: ReviewRepository
}

class MallAppDataContainer(override val context: Context) : AppContainer {
    override val assetRepository by lazy { AssetRepository(this) }
    override val destinationRepository by lazy { DestinationRepository(this) }
    override val firebaseStore by lazy { FirebaseFirestore.getInstance() }
    override val firebaseStorage by lazy { FirebaseStorage.getInstance() }
    override val profileRepository by lazy { ProfileRepository(this) }
    override val regionApi by lazy { ApiClient.getRegionApi() }
    override val regionDao by lazy { RegionDatabase.getDatabase(context).regionDao() }
    override val regionRepository by lazy { RegionRepository(this) }
    override val userRepository by lazy { UserRepository(this) }
    override val userSharedPreferences by lazy {
        context.getSharedPreferences(
            Constant.SP_USER,
            Context.MODE_PRIVATE
        )
    }
    override val tripRepository by lazy { TripRepository(this) }
    override val reviewRepository by lazy { ReviewRepository(this) }

}

fun CreationExtras.mallApplication(): TravelApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TravelApplication)
