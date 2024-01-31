package com.beaconfire.travel.repo.data

import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip

data class TripData(
    val owner: String? = null,
    val destinations: List<String>? = emptyList(),
    val collaborators: List<String>? = null,
    val duration: String? = null,
    val title: String? = null,
    val description: String? = null,
    val visibility: String? = null, //visibility can be either "private" or "public"
    val numPeople: Long? = 1
) {
    fun toTrip(documentId: String) = Trip(
        tripId = documentId,
        owner = owner ?: "",
        destinations = emptyList(),
        collaborators = collaborators ?: emptyList(),
        duration = duration ?: "",
        title = title ?: "",
        description = description ?: "",
        visibility = visibility ?: "",
        numPeople = numPeople ?: 1
    )
}
