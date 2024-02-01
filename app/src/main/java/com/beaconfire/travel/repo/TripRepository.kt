package com.beaconfire.travel.repo

import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.data.TripData
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class TripRepository(private val appContainer: AppContainer) {
    suspend fun getAllTrips(): List<Trip> = appContainer.userRepository.getLoginUser()?.let {
        queryTrips(it.userId).map { tripData ->
            tripData.toTrip(
                destinations = appContainer.destinationRepository.getAllDestinations(tripData.destinations)
            )
        }
    } ?: emptyList()

    suspend fun createTrip() = appContainer.userRepository.getLoginUser()?.let {
        callbackFlow {
            val tripData = TripData(
                owner = it.userId,
                duration = "duration",
                title = "title",
                visibility = Trip.Visibility.Public().value
            )
            val tripRef = appContainer.firebaseStore.collection("trip").document()
            tripRef.set(tripData)
                .addOnSuccessListener {
                    trySend(
                        tripData.copy(tripId = tripRef.id).toTrip(emptyList())
                    )
                }
                .addOnFailureListener { trySend(null) }
                .await()
            awaitClose()
        }.first()
    }

    suspend fun removeDestination(trip: Trip, destination: Destination) = callbackFlow {
        val tripRef = appContainer.firebaseStore.collection("trip").document(trip.tripId)
        tripRef.update("destinations", FieldValue.arrayRemove(destination.destinationId))
            .addOnSuccessListener { trySend(true) }
            .addOnFailureListener { trySend(false) }
            .await()
        awaitClose()
    }.first()

    suspend fun addDestination(trip: Trip, destination: Destination) = callbackFlow {
        val tripRef = appContainer.firebaseStore.collection("trip").document(trip.tripId)
        tripRef.update("destinations", FieldValue.arrayUnion(destination.destinationId))
            .addOnSuccessListener { trySend(true) }
            .addOnFailureListener { trySend(false) }
            .await()
        awaitClose()
    }.first()

    suspend fun deleteTrip(trip: Trip) = callbackFlow {
        appContainer.firebaseStore.collection("trip").document(trip.tripId)
            .delete()
            .addOnSuccessListener { trySend(true) }
            .addOnFailureListener { trySend(false) }
            .await()
        awaitClose()
    }.first()

    suspend fun updateTripVisibility(trip: Trip) = callbackFlow {
        val tripRef = appContainer.firebaseStore.collection("trip").document(trip.tripId)
        appContainer.firebaseStore.runTransaction { transaction ->
            transaction.update(tripRef, "visibility", trip.visibility.value)
        }
            .addOnSuccessListener { trySend(true) }
            .addOnFailureListener { trySend(false) }
            .await()
        awaitClose()
    }.first()

    private suspend fun queryTrips(userId: String) = callbackFlow<List<TripData>> {
        appContainer.firebaseStore.collection("trip")
            .whereEqualTo("owner", userId)
            .get()
            .addOnSuccessListener { documents -> trySend(documents.mapNotNull { it.toTripData() }) }
            .addOnFailureListener { trySend(emptyList()) }
            .await()
        awaitClose()
    }.first()

    private fun DocumentSnapshot.toTripData() =
        toObject(TripData::class.java)?.copy(tripId = id)
}
