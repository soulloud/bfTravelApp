package com.beaconfire.travel.repo.data

import com.beaconfire.travel.repo.model.Profile

data class ProfileData(
    val profileId: String = "",
    val fullName: String = "",
    val location: String = "",
    val joinDate: String = "",
    val photoImage: String = "",
    val aboutYou: String = "",
    val uploadedPhotos: List<String> = emptyList()
) {
    fun toProfile() = Profile(
        profileId = profileId,
        fullName = fullName,
        location = location,
        joinDate = joinDate,
        photoImage = photoImage,
        aboutYou = aboutYou,
        uploadedPhotos = uploadedPhotos,
    )
}

fun Profile.toProfileData() = ProfileData(
    fullName = fullName,
    location = location,
    joinDate = joinDate,
    photoImage = photoImage,
    aboutYou = aboutYou,
    uploadedPhotos = uploadedPhotos,
)
