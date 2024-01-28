package com.beaconfire.travel.repo.region

class RegionRepository(
    apiClient: ApiClient
) {
    private val regionApi = apiClient.getRegionApi()

    suspend fun getAllStates(): List<String> {
        return regionApi.allStates().mapNotNull { it.state_name }
    }

    suspend fun getAllCitiesForStatus(city: String): List<String> {
        return regionApi.allCitiesForStates(city).mapNotNull { it.city_name }
    }
}
