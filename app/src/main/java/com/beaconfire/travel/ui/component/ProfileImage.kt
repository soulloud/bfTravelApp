package com.beaconfire.travel.ui.component

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileImage(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.size(96.dp),
    imageUri: Uri?,
) {
    Image(
        painter = rememberAsyncImagePainter(imageUri),
        contentDescription = "Profile",
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(shape = CircleShape)
    )
}