package com.beaconfire.travel.repo.data

import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Price

data class DestinationData (
    val name: String? = null,
    val ownerOrOrganization: String? = null,
    val location: String? = null,
    val description: String? = null,
    val reviewList: List<String>? = null, // Assuming reviews are represented as strings
    val price: Map<String, String>? = emptyMap(),
    val localLanguages: List<String>? = null,
    val ageRecommendation: String? = null,
    val recommendedByPercentage: String? = null,
) {
    fun toDestination() = Destination(
        name = name ?: "",
        ownerOrOrganization = ownerOrOrganization ?: "",
        location = location ?: "",
        description = description ?: "",
        reviewList = reviewList ?: emptyList(),
        price = price?.toPrice() ?: Price.InvalidPrice,
        localLanguages = localLanguages ?: emptyList(),
        ageRecommendation = ageRecommendation ?: "",
        recommendedByPercentage = recommendedByPercentage ?: "",
    )
}

private fun Map<String, String>.toPrice(): Price {
    val value = get("value")
    val currency = get("currency")
    return Price(value = value?.toDouble() ?: -1.0, currency = currency?:"USD")
}
