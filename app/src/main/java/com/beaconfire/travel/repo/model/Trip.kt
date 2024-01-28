package com.beaconfire.travel.repo.model

data class Trip(
    val destinationList: List<Destination>? = null,
    val collaborators: List<String>? = null,
    val duration: String? = null,
    val title: String? = null,
    val description: String? = null,
    val visibility: String? = null //visibility can be either "private" or "public"
)
