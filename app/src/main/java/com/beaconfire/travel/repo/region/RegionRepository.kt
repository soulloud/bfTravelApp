package com.beaconfire.travel.repo.region

import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.model.City
import com.beaconfire.travel.repo.model.State

class RegionRepository(private val appContainer: AppContainer) {
    suspend fun getStates(): List<State> {
        val statesFromDB = appContainer.regionDao.getAllStates()
        if (statesFromDB.isNotEmpty()) {
            return statesFromDB.map { State.fromStateEntity(it) }
        }
        val states = appContainer.regionApi.allStates().map { State.fromString(it.state_name) }
        appContainer.regionDao.insertStates(states.map { it.toStateEntity() })
        return appContainer.regionDao.getAllStates().map { State.fromStateEntity(it) }
    }

    suspend fun getCities(state: State): List<City> {
        val citiesFromDB = appContainer.regionDao.findCitiesByState(state.id!!)
        if (citiesFromDB.isNotEmpty()) {
            return citiesFromDB.map { City.fromCityEntity(it) }
        }
        val cities = appContainer.regionApi.allCitiesForState(state.state)
            .map { City.fromString(state, it.city_name) }
        appContainer.regionDao.insertCities(cities.map { it.toCityEntity() })
        return appContainer.regionDao.findCitiesByState(state.id).map { City.fromCityEntity(it) }
    }
}
