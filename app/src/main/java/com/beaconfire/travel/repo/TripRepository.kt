package com.beaconfire.travel.repo

import android.util.Log
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.data.DestinationData
import com.beaconfire.travel.repo.data.TripData
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class TripRepository(val appContainer: AppContainer) {

    private val db = FirebaseFirestore.getInstance()
    suspend fun getAllTrips(userId: String): List<Trip> = getAllTripsWithoutDestination(userId).map { trip ->
            trip.copy(destinations = appContainer.destinationRepository.getDestinations())
        }

    private suspend fun getAllTripsWithoutDestination(userId: String) = callbackFlow {
        db.collection("trip")
            .whereEqualTo("owner", userId)
            .get()
            .addOnSuccessListener { documents ->
                val trips = documents.map { it.toObject(TripData::class.java).toTrip(it.id) }
                trySend(trips)
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
        db.collection("trip")
    }

    suspend fun deleteCurrentTrip(tripId: String) = callbackFlow{
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

    suspend fun changeTripVisibility(tripId: String) = callbackFlow{
        val tripRef = db.collection("trip").document(tripId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(tripRef)
            val currentVisibility: String = snapshot.getString("visibility")!!
            if (currentVisibility == "public"){
                transaction.update(tripRef, "visibility", "private")
            }
            else {
                transaction.update(tripRef, "visibility", "public")
            }
        }
            .addOnSuccessListener {
            trySend("Successful")
        }
            .await()
        awaitClose()
    }
}