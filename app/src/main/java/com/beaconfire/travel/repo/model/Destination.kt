package com.beaconfire.travel.repo.model

data class Destination(
    val name: String? = null,
    val ownerOrOrganization: String? = null,
    val location: String? = null,
    val description: String? = null,
    val reviewList: List<String>? = null, // Assuming reviews are represented as strings
    //val price: Price? = null,
    val localLanguages: List<String>? = null,
    val ageRecommendation: String? = null,
    val recommendedByPercentage: String? = null
)
