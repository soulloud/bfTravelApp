package com.beaconfire.travel.repo.model

data class Trip(
    val tripId: String,
    val owner: String? = null,
    val destinationList: List<String>? = null, // list of destination ids
    val collaborators: List<String>? = null,
    val duration: String? = null,
    val title: String? = null,
    val description: String? = null,
    val visibility: String = "public",
    val numPeople: Long? = 1
)
