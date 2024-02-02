package com.beaconfire.travel.ui.component.button

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        Button(
            modifier = Modifier.width(200.dp),
            onClick = { launcher.launch("image/*")
        }) {
            Text(text = "Pick from library")
        }
    }
}
