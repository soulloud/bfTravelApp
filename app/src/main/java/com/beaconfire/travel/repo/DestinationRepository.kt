package com.beaconfire.travel.repo

import android.util.Log
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class DestinationRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getCurrentDestination(searchText: String): Destination = callbackFlow {
        db.collection("destination")
            .whereEqualTo("name", searchText)
            .get()
            .addOnSuccessListener { documents ->
                val result = documents.first().toObject(Destination::class.java)
                trySend(result)
            }
//            .addOnFailureListener{
//                trySend()
//            }
            .await()
        awaitClose()
    }.first()

    suspend fun getAllTrips(username: String): List<Trip> = callbackFlow{
        db.collection("trip")
            .whereEqualTo("owner", username)
            .get()
            .addOnSuccessListener { documents ->
                val result = documents.toObjects(Trip::class.java)
                trySend(result)
            }
            .await()
        awaitClose()
    }.first()
}