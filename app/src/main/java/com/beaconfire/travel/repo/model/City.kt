package com.beaconfire.travel.repo.model

import com.beaconfire.travel.repo.database.region.CityEntity

data class City(
    val id: Long? = null,
    val stateId: Long,
    val city: String
) {
    fun toCityEntity() = CityEntity(stateId = stateId, city = city)

    companion object {
        val INVALID_CITY = City(stateId = -1, city = "")
        fun fromString(state: State, city: String) = City(stateId = state.id!!, city = city)

        fun fromCityEntity(cityEntity: CityEntity) =
            City(stateId = cityEntity.stateId, city = cityEntity.city)
    }
}
