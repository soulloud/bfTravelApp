package com.beaconfire.travel.repo.model

data class Profile(
    val fullName: String,
    val location: String,
    val joinDate: String,
    val photoImage: String,
    val about: String,
    val uploadedPhotos: List<String>
)

