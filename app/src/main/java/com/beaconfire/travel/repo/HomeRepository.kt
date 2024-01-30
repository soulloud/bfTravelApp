package com.beaconfire.travel.repo

import android.graphics.BitmapFactory
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.data.DestinationData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class HomeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage
    private val storageUrl = "gs://beaconfiretripapp.appspot.com"

    suspend fun getDestinations() = callbackFlow<List<Destination>> {
        db.collection("destination")
            .get()
            .addOnSuccessListener { documents ->
                val result = documents.toObjects(DestinationData::class.java).map { it.toDestination() }
                trySend(result)
            }
            .addOnFailureListener{
                trySend(emptyList())
            }
            .await()
        awaitClose()
    }.first()

    suspend fun searchDestination(searchText: String) = callbackFlow<List<Destination>> {
        db.collection("DestinationPojo")
            .whereEqualTo("name", searchText)
            //.whereArrayContains("location", searchText)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val result = documents.toObjects(DestinationData::class.java).map { it.toDestination() }
                    trySend(result)
                }
            }
            .addOnFailureListener {
                trySend(emptyList())
            }
            .await()
        awaitClose()
    }.first()

    suspend fun getImage() = callbackFlow {
        // Points to the root reference
        val storageRef = storage.reference
        val MEGABYTE: Long = 1024 * 1024

        val imagesRef = storageRef.child("newyork.jpg")
        imagesRef.getBytes(MEGABYTE)
            .addOnSuccessListener {bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                trySend(bitmap)
            }
            .await()
        awaitClose()
    }


}