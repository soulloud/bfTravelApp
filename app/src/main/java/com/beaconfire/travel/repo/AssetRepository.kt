package com.beaconfire.travel.repo

import android.net.Uri
import com.beaconfire.travel.AppContainer
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.io.File

class AssetRepository(private val appContainer: AppContainer) {
    suspend fun uploadImageAsset(uri: Uri) = callbackFlow {
        val file = File(uri.toString())
        val timestamp = System.currentTimeMillis()
        val fileExt = file.path.substring(file.path.lastIndexOf(".") + 1)
        val filename = "$timestamp.$fileExt"
        val storageRef = generateStorageReference(filename)
        storageRef.putFile(uri)
            .addOnSuccessListener { trySend(filename) }
            .addOnFailureListener { trySend(null) }
            .await()
        awaitClose()
    }.first()

    suspend fun fetchImageAsset(filename: String) = callbackFlow {
        generateStorageReference(filename)
            .downloadUrl
            .addOnSuccessListener { trySend(it) }
            .addOnFailureListener { trySend(null) }
            .await()
        awaitClose()
    }.first()

    private suspend fun generateStorageReference(fileName: String): StorageReference {
        val userId = appContainer.userRepository.getLoginUser()?.userId ?: "tmp"
        val filename = "assets/$userId/$fileName"
        return appContainer.firebaseStorage.reference.child(filename)
    }

    companion object {
        val TAG = AssetRepository::class.java.simpleName
    }
}
