package com.beaconfire.travel.repo

import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.data.ProfileData
import com.beaconfire.travel.repo.data.toProfileData
import com.beaconfire.travel.repo.model.Profile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class ProfileRepository(private val appContainer: AppContainer) {

    suspend fun queryProfile(profileId: String) = callbackFlow {
        appContainer.firebaseStore.collection("profile").document(profileId)
            .get()
            .addOnSuccessListener {
                trySend(it.toObject(ProfileData::class.java)?.toProfile())
            }
            .addOnFailureListener { trySend(null) }
            .await()
        awaitClose()
    }.first()


    suspend fun createProfile(profile: Profile): Profile? = callbackFlow {
        val profileRef = appContainer.firebaseStore.collection("profile").document()
        profileRef.set(profile.toProfileData())
            .addOnSuccessListener {
                trySend(profile.copy(profileId = profileRef.id))
            }
            .addOnFailureListener {
                trySend(null)
            }
            .await()
        awaitClose()
    }.first()

    suspend fun updateProfile(userId: String, profile: Profile) {
        appContainer.firebaseStore.collection("profile").document(userId)
            .set(profile.toProfileData()).await()
    }

//    suspend fun uploadProfileImage(userId: String, imageUri: Uri): String {
//        val ref = storage.reference.child("profile_images/$userId.jpg")
//        ref.putFile(imageUri).await()
//        val downloadUrl = ref.downloadUrl.await().toString()
//
//        // Update the photoImage field in the Firestore profile document
//        val profileRef = db.collection("profile").document(userId)
//        profileRef.update("photoImage", downloadUrl).await()
//
//        return downloadUrl
//    }
//
//    suspend fun uploadAdditionalPhoto(userId: String, imageUri: Uri): String {
//        // Create a unique file name for each photo
//        val fileName = "uploaded_photos/${userId}_${System.currentTimeMillis()}.jpg"
//        val ref = storage.reference.child(fileName)
//        ref.putFile(imageUri).await()
//        val downloadUrl = ref.downloadUrl.await().toString()
//
//        // Add the photo URL to the uploadedPhotos list in the Firestore profile document
//        val profileRef = db.collection("profile").document(userId)
//        profileRef.update("uploadedPhotos", FieldValue.arrayUnion(downloadUrl)).await()
//
//        return downloadUrl
//    }
}