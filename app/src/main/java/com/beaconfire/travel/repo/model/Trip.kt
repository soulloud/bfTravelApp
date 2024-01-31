package com.beaconfire.travel.repo.model

data class Trip(
    val tripId: String,
    val owner: String = "",
    val destinations: List<Destination> = emptyList(),
    val collaborators: List<String> = emptyList(),
    val duration: String = "",
    val title: String = "",
    val description: String = "",
    val visibility: String = "public",
    val numPeople: Long = 1,
)
