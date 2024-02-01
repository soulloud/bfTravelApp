package com.beaconfire.travel.repo

import android.net.Uri
import com.beaconfire.travel.AppContainer
import java.io.File

class AssetRepository(private val appContainer: AppContainer) {
    suspend fun uploadImageAsset(uri: Uri): String {
        val file = File(uri.toString())
        val userId = appContainer.userRepository.getLoginUser()?.userId ?: "tmp"
        val timestamp = System.currentTimeMillis()
        val fileExt = file.path.substring(file.path.lastIndexOf(".") + 1)
        val fileName = "assets/$userId/$timestamp.$fileExt"
        val ref = appContainer.firebaseStorage.reference.child(fileName)
        ref.putFile(uri)
        return ref.downloadUrl.toString()
    }
}
