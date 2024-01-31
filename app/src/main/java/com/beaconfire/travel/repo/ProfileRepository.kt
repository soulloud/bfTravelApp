package com.beaconfire.travel.repo

import android.net.Uri
import android.util.Log
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.UserRepository.Companion.TAG
import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.utils.SessionManager
import com.beaconfire.travel.utils.SessionManager.userId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class ProfileRepository(val appContainer: AppContainer) {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun getProfileForUser() = callbackFlow {
        val userId = SessionManager.userId
        if (userId != null) {
            val userRef = db.collection("user").document(userId)
            userRef.get()
                .addOnSuccessListener { userSnapshot ->
                    if (userSnapshot.exists()) {
                        val userProfilePath = userSnapshot.getString("profile")
                        userProfilePath?.let {
                            val profileRef = db.document(it)
                            profileRef.get()
                                .addOnSuccessListener { profileSnapshot ->
                                    if (profileSnapshot.exists()) {
                                        val profileData = profileSnapshot.toObject(Profile::class.java)
                                        trySend(profileData)
                                    } else {
                                        Log.d(TAG, "Profile not found")
                                        trySend(null) // Profile not found
                                    }
                                }
                        } ?: run {
                            Log.d(TAG, "Profile path is null")
                            trySend(null) // Profile path is null
                        }
                    } else {
                        Log.d(TAG, "User not found")
                        trySend(null) // User not found
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Failed to fetch user: $it")
                    trySend(null)
                }
        } else {
            Log.d(TAG, "SessionManager userId is null")
            trySend(null) // SessionManager userId is null
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
        val ref = storage.reference.child("profile_images/$userId.jpg")
        ref.putFile(imageUri).await()
        val downloadUrl = ref.downloadUrl.await().toString()

        // Update the photoImage field in the Firestore profile document
        val profileRef = db.collection("profile").document(userId)
        profileRef.update("photoImage", downloadUrl).await()

        return downloadUrl
    }

    suspend fun uploadAdditionalPhoto(userId: String, imageUri: Uri): String {
        // Create a unique file name for each photo
        val fileName = "uploaded_photos/${userId}_${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(fileName)
        ref.putFile(imageUri).await()
        val downloadUrl = ref.downloadUrl.await().toString()

        // Add the photo URL to the uploadedPhotos list in the Firestore profile document
        val profileRef = db.collection("profile").document(userId)
        profileRef.update("uploadedPhotos", FieldValue.arrayUnion(downloadUrl)).await()

        return downloadUrl
    }


}