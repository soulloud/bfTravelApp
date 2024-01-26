package com.beaconfire.travel

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.beaconfire.travel.repo.UserRepository

interface AppContainer {
    val userRepository: UserRepository
}

class MallAppDataContainer(
    context: Context
) : AppContainer {
    override val userRepository by lazy { UserRepository() }
}

fun CreationExtras.mallApplication(): TravelApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TravelApplication)
