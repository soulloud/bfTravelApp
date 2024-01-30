package com.beaconfire.travel.repo

import android.net.Uri
import android.util.Log
import com.beaconfire.travel.repo.UserRepository.Companion.TAG
import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.utils.SessionManager.userId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class ProfileRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun getProfileForUser(user: String) = callbackFlow {
        val profileRef = userId?.let { db.collection("profiles").document(it) }

        profileRef?.get()?.addOnSuccessListener { profileSnapshot ->
            if (profileSnapshot.exists()) {
                val profileData = profileSnapshot.toObject(Profile::class.java)
                trySend(profileData)
            } else {
                trySend(null) // Profile not found
            }
        }?.addOnFailureListener {
            Log.d(TAG, "Failed to fetch profile: $it")
            trySend(null)
        }

        awaitClose()
    }.first()

    suspend fun updateProfile(userId: String, profile: Profile) {
        db.collection("profile").document(userId).set(profile).await()
    }

    suspend fun uploadProfileImage(userId: String, imageUri: Uri): String {
        val ref = storage.reference.child("photoImage/$userId.jpg")
        ref.putFile(imageUri).await()
        return ref.downloadUrl.await().toString()
    }


}