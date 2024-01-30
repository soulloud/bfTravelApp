package com.beaconfire.travel.repo.data

data class UserData(
    var userId: String? = null,
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val currency: String = "USD",
    val reviewList: List<String> = listOf(),
    val tripList: List<String> = listOf(),
    val saveList: List<String> = listOf(),
    val profile: String? = null // Assuming this will be a reference or ID to a profile document
)
