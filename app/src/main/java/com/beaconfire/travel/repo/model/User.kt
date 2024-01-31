package com.beaconfire.travel.repo.model

data class User(
    val userId: String,
    val displayName: String,
    val email: String,
    val password: String,
    val currency: String,
    val reviews: List<String>,
    val trips: List<String>,
    val saves: List<String>,
    val profile: Profile,
)
