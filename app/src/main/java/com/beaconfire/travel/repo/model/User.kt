package com.beaconfire.travel.repo.model

data class User(
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val currency: String = "USD",
    val reviewList: List<String> = listOf(),
    val tripList: List<String> = listOf(),
    val saveList: List<String> = listOf(),
    val profile: String? = null // Assuming this will be a reference or ID to a profile document
){
    companion object {
        val INVALID_USER = User() // Represents an invalid user
    }
}

