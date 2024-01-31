package com.beaconfire.travel.repo

import android.graphics.BitmapFactory
import com.beaconfire.travel.repo.data.DestinationData
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class DestinationRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getCurrentDestination(searchText: String): Destination? = callbackFlow {
        db.collection("destination")
            .whereEqualTo("name", searchText)
            .get()
            .addOnSuccessListener { documents ->
                val result = documents.first().toObject(Destination::class.java)
                trySend(result)
            }
            .addOnFailureListener {
                trySend(null)
            }
            .await()
        awaitClose()
    }.first()

    suspend fun getAllTrips(username: String): List<Trip> = callbackFlow {
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

    private val storage = Firebase.storage
    private val storageUrl = "gs://beaconfiretripapp.appspot.com"

    suspend fun getDestinations() = callbackFlow<List<Destination>> {
        db.collection("destination")
            .get()
            .addOnSuccessListener { documents ->
                val result: MutableList<Destination> = ArrayList()
                for (document in documents) {
                    result.add(
                        document.toObject(DestinationData::class.java).toDestination(document.id)
                    )
                }
                trySend(result)
            }
            .addOnFailureListener {
                trySend(emptyList())
            }
            .await()
        awaitClose()
    }.first()

    suspend fun getDestinations(destinationIds: List<String>) =
        destinationIds.mapNotNull { getDestination(it) }

    suspend fun getDestination(destinationId: String) = callbackFlow<Destination?> {
        db.collection("destination")
            .document(destinationId)
            .get()
            .addOnSuccessListener { document ->
                val destination =
                    document.toObject(DestinationData::class.java)?.toDestination(document.id)
                trySend(destination)
            }
            .addOnFailureListener {
                trySend(null)
            }
            .await()
        awaitClose()
    }.first()

    suspend fun searchDestination(searchText: String) = callbackFlow<List<Destination>> {
        db.collection("destination")
            .whereEqualTo("name", searchText)
            //.whereArrayContains("location", searchText)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val result: MutableList<Destination> = ArrayList()
                    for (document in documents) {
                        result.add(
                            document.toObject(DestinationData::class.java)
                                .toDestination(document.id)
                        )
                    }
                }
            }
            .addOnFailureListener {
                trySend(emptyList())
            }

        suspend fun getImage() = callbackFlow {
            // Points to the root reference
            val storageRef = storage.reference
            val MEGABYTE: Long = 1024 * 1024

            val imagesRef = storageRef.child("newyork.jpg")
            imagesRef.getBytes(MEGABYTE)
                .addOnSuccessListener { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    trySend(bitmap)
                }
                .await()
            awaitClose()
        }
    }
}
