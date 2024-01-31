package com.beaconfire.travel.repo

import android.util.Log
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.data.TripData
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class TripRepository(private val appContainer: AppContainer) {

    private val db = FirebaseFirestore.getInstance()
    suspend fun getAllTrips(userId: String): List<Trip> = getTripDatas(userId).map { trip ->
        trip.toTrip(destinations = appContainer.destinationRepository.getDestinations(trip.destinations))
    }

    private suspend fun getTripDatas(userId: String) = callbackFlow<List<TripData>> {
        db.collection("trip")
            .whereEqualTo("owner", userId)
            .get()
            .addOnSuccessListener { documents ->
                val trips = documents.map { it.toObject(TripData::class.java).copy(tripId = it.id) }
                trySend(trips)
            }
            .addOnFailureListener {
                trySend(emptyList())
            }
            .await()
        awaitClose()
    }.first()

    suspend fun createNewTrip(tripData: TripData) = callbackFlow {
        db.collection("trip")
            .add(tripData)
            .addOnSuccessListener {
                Log.d("test", "new trip created!")
                trySend("new trip created!")
            }
            .addOnFailureListener {
                Log.d("test", "something went wrong on creating new trip")
                trySend("something went wrong on creating new trip")
            }
            .await()
        awaitClose()
    }.first()

    suspend fun removeFromCurrentTrip(destination: Destination, trip: Trip) {


        val destinationToDelete = "aHmLo7Vy6WLAWKRNbCF9"
        val tripRef = db.collection("trip").document(trip.tripId)
        tripRef.update("destinations", FieldValue.arrayRemove(destinationToDelete)).await()

    }

    suspend fun deleteCurrentTrip(tripId: String) = callbackFlow {
        db.collection("trip").document(tripId)
            .delete()
            .addOnSuccessListener {
                Log.d("test", "trip deleted")
                trySend("trip deleted")
            }
            .addOnFailureListener {
                Log.d("test", "something went wrong on deleting trip")
            }
            .await()
        awaitClose()
    }.first()

    suspend fun changeTripVisibility(tripId: String) = callbackFlow {
        val tripRef = db.collection("trip").document(tripId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(tripRef)
            val currentVisibility: String = snapshot.getString("visibility")!!
            val newVisibility = if (currentVisibility == "public") "private" else "public"
            transaction.update(tripRef, "visibility", newVisibility)
        }
            .addOnSuccessListener {
                Log.d("test", "changed")
                trySend("Successful")
            }
            .await()
        awaitClose()
    }.first()
}
