package com.beaconfire.travel.repo.model

data class Destination(
    val destinationId: String?,
    val name: String,
    val location: String,
    val description: String,
    val reviews: List<String>,
    val price: Price,
    val rating: Double,
    val tags: List<String>,
    val images: List<String>,
)
