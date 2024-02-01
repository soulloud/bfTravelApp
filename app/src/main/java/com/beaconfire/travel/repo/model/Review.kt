package com.beaconfire.travel.repo.model

data class Review(
    val destination: String,
    val score: String,
    val title: String,
    val description: String,
    val timestamp: Long
)
