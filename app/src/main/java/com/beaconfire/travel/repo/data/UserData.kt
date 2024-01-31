package com.beaconfire.travel.repo.data

import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.repo.model.User

data class UserData(
    var userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val currency: String = "USD",
    val reviews: List<String> = listOf(),
    val trips: List<String> = listOf(),
    val saves: List<String> = listOf(),
    val profile: String? = null,
) {
    fun toUser(profile: Profile) = User(
        userId = userId,
        displayName = displayName,
        email = email,
        password = password,
        currency = currency,
        reviews = reviews,
        trips = trips,
        saves = saves,
        profile = profile,
    )
}

fun User.toUserData() = UserData(
    userId = userId,
    displayName = displayName,
    email = email,
    password = password,
    currency = currency,
    reviews = reviews,
    trips = trips,
    saves = saves,
    profile = profile.profileId,
)
