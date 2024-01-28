package com.beaconfire.travel.repo.region

import retrofit2.http.GET
import retrofit2.http.Path

interface RegionApi {

    @GET("states/United States")
    suspend fun allStates(): List<StateResponse>

    @GET("cities/{state}")
    suspend fun allCitiesForState(@Path(value = "state") state: String): List<CityResponse>
}

data class StateResponse(val state_name: String)
data class CityResponse(val city_name: String)
