package com.beaconfire.travel

import android.app.Application

class TravelApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = MallAppDataContainer(this)
    }
}
