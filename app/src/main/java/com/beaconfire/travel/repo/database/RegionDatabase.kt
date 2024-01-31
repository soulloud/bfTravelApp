package com.beaconfire.travel.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.beaconfire.travel.repo.database.region.CityEntity
import com.beaconfire.travel.repo.database.region.RegionDao
import com.beaconfire.travel.repo.database.region.StateEntity

@Database(entities = [CityEntity::class, StateEntity::class], version = 1, exportSchema = false)
abstract class RegionDatabase : RoomDatabase() {
    abstract fun regionDao(): RegionDao

    companion object {
        @Volatile
        private var instance: RegionDatabase? = null

        fun getDatabase(context: Context): RegionDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, RegionDatabase::class.java, "region_database")
                    .build()
                    .also { instance = it }
            }
        }
    }
}
