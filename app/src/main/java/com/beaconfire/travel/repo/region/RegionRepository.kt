package com.beaconfire.travel.repo.region

import com.beaconfire.travel.repo.database.region.RegionDao
import com.beaconfire.travel.repo.model.City
import com.beaconfire.travel.repo.model.State

class RegionRepository(
    private val regionApi: RegionApi,
    private val regionDao: RegionDao
) {
    suspend fun getStates(): List<State> {
        val statesFromDB = regionDao.getAllStates()
        if (statesFromDB.isNotEmpty()) {
            return statesFromDB.map { State.fromStateEntity(it) }
        }
        val states = regionApi.allStates().map { State.fromString(it.state_name) }
        regionDao.insertStates(states.map { it.toStateEntity() })
        return regionDao.getAllStates().map { State.fromStateEntity(it) }
    }

    suspend fun getCities(state: State): List<City> {
        val citiesFromDB = regionDao.findCitiesByState(state.id!!)
        if (citiesFromDB.isNotEmpty()) {
            return citiesFromDB.map { City.fromCityEntity(it) }
        }
        val cities = regionApi.allCitiesForState(state.state).map { City.fromString(state, it.city_name) }
        regionDao.insertCities(cities.map { it.toCityEntity() })
        return regionDao.findCitiesByState(state.id).map { City.fromCityEntity(it) }
    }
}
