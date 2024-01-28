package com.beaconfire.travel.repo.database.region

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegionDao {

    @Insert
    suspend fun insertStates(states: List<StateEntity>)
    @Insert
    suspend fun insertCities(states: List<CityEntity>)

    @Query("SELECT * FROM city WHERE state_id = :stateId")
    suspend fun findCitiesByState(stateId: Long): List<CityEntity>

    @Query("SELECT * FROM state")
    suspend fun getAllStates(): List<StateEntity>
}
