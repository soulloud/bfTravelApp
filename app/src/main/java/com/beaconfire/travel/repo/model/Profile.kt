package com.beaconfire.travel.repo.model

data class Profile(
    val fullName: String = "",
    val location: String = "",
    val joinDate: String = "", // Format: YYYY-MM-DD or similar
    val photoImage: String = "", // URL or reference to Firebase Storage
    val aboutYou: String = "",
    val uploadedPhotos: List<String> = listOf() // URLs or references to Firebase Storage
)


