package com.beaconfire.travel.repo.model

data class User(
    val username: String = "",
    val password: String = "",
) {
    companion object {
        val INVALID_USER = User()
        val DUMMY_VALID_USER = User(username = "valid", password = "")
    }
}


// Replace this with current user class after we confirm the schema
data class UserClassToBeReplaced(
    val displayName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val reviewList: List<Review>? = null,
    val tripList: List<Trip>? = null,
    val saveList: List<String>? = null,
    val currency: String? = null,
    val profile: Profile? = null
)
