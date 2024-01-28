package com.beaconfire.travel.repo.model

data class Destination(
    val name: String,
    val ownerOrOrganization: String,
    val location: String,
    val description: String,
    val reviewList: List<String>, // Assuming reviews are represented as strings
    val price: Price,
    val localLanguages: List<String>,
    val ageRecommendation: String,
    val recommendedByPercentage: String
)
