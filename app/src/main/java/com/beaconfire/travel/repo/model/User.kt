package com.beaconfire.travel.repo.model

import com.beaconfire.travel.R

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
        val mockUsers = listOf(
            User(
                displayName = "John Doe",
                email = "john@example.com",
                password = "password123",
                currency = "USD",
                reviewList = listOf("Review 1", "Review 2"),
                tripList = listOf("Trip 1", "Trip 2"),
                saveList = listOf("Save 1", "Save 2"),
                profile = "profile_id_1"
            ),
            User(
                displayName = "David",
                email = "john@example.com",
                password = "password123",
                currency = "USD",
                reviewList = listOf("Review 1", "Review 2"),
                tripList = listOf("Trip 1", "Trip 2"),
                saveList = listOf("Save 1", "Save 2"),
                profile = "profile_id_1"
            ),
            User(
                displayName = "Su",
                email = "john@example.com",
                password = "password123",
                currency = "USD",
                reviewList = listOf("Review 1", "Review 2"),
                tripList = listOf("Trip 1", "Trip 2"),
                saveList = listOf("Save 1", "Save 2"),
                profile = "profile_id_1"
            ),
            User(
                displayName = "Alice Smith",
                email = "alice@example.com",
                password = "alice_password",
                currency = "EUR",
                reviewList = listOf("Review 3", "Review 4"),
                tripList = listOf("Trip 3", "Trip 4"),
                saveList = listOf("Save 3", "Save 4"),
                profile = "profile_id_2"
            ),
            User(
                displayName = "Bob Johnson",
                email = "bob@example.com",
                password = "bob_password",
                currency = "GBP",
                reviewList = listOf("Review 5", "Review 6"),
                tripList = listOf("Trip 5", "Trip 6"),
                saveList = listOf("Save 5", "Save 6"),
                profile = "profile_id_3"
            )
        )

        val mockProfileIcons = listOf(
            R.drawable.ic_profile_alice,
            R.drawable.ic_profile_mia,
            R.drawable.ic_profile_mike,
            R.drawable.ic_profile_shali,
            R.drawable.ic_profile_shuaige
        )
    }
}
