package com.beaconfire.travel.ui.component.button

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

typealias OnPhotoPicked = (Uri) -> Unit

@Composable
fun GalleryPhotoPickerButton(onPhotoPicked: OnPhotoPicked) {
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onPhotoPicked(it) }
    }
    Column {
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "Pick image")
        }
    }
}
