package com.beaconfire.travel.repo.model

data class Review(
    val reviewId: String,
    val destination: String,
    val score: Double,
    val title: String,
    val description: String,
    val timestamp: String,
    val owner: String
)
