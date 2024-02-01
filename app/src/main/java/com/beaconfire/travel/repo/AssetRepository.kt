package com.beaconfire.travel.repo

import com.beaconfire.travel.AppContainer

class AssetRepository(private val appContainer: AppContainer) {
//    suspend fun uploadImageAsset(userId: String, ): String {
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