package com.beaconfire.travel.repo.data

import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip

data class TripData(
    val tripId: String = "",
    val owner: String? = null,
    val destinations: List<String> = emptyList(),
    val collaborators: List<String>? = null,
    val duration: String? = null,
    val title: String? = null,
    val description: String? = null,
    val visibility: String? = null,
    val numPeople: Long? = 1
) {
    fun toTrip(destinations: List<Destination>) = Trip(
        tripId = tripId,
        owner = owner ?: "",
        destinations = destinations,
        collaborators = collaborators ?: emptyList(),
        duration = duration ?: "",
        title = title ?: "",
        description = description ?: "",
        visibility = Trip.Visibility.fromString(visibility),
        numPeople = numPeople ?: 1
    )
}

fun Trip.toTripData() = TripData(
    tripId = tripId,
    owner = owner,
    destinations = destinations.mapNotNull { it.destinationId },
    collaborators = collaborators,
    duration = duration,
    title = title,
    description = description,
    visibility = visibility.value,
    numPeople = numPeople
)
