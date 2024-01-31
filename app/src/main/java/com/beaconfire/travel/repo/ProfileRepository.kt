package com.beaconfire.travel.repo

import android.net.Uri
import android.util.Log
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.UserRepository.Companion.TAG
import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.utils.SessionManager
import com.beaconfire.travel.utils.SessionManager.userId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class ProfileRepository(val appContainer: AppContainer) {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun getProfileForUser(user: String) = callbackFlow {
        // TODO: Not a good idea to save user id as global, but let's refactor later
//        val user = userId?.let { appContainer.userRepository.fetchUser(it) }


        // TODO: @David similar to userRepository.fetchUser to get ProfileData and then convert to Profile object
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

    private suspend fun createProfile(profile: Profile) = callbackFlow {
        val profileRef = db.collection("profile").document()
        profileRef.set(profile)
            .addOnSuccessListener {
                trySend(profileRef.path)
            }
            .addOnFailureListener {
                trySend(null)
            }
            .await()
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