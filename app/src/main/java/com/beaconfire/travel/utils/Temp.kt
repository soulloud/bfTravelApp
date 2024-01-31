package com.beaconfire.travel.utils

import android.graphics.BitmapFactory
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class Temp {
    private val storageUrl = "gs://beaconfiretripapp.appspot.com"
    suspend fun getImage(storage: FirebaseStorage) = callbackFlow {
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