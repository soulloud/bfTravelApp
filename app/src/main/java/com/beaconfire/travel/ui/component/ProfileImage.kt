package com.beaconfire.travel.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ProfileImage(
    image: Int,
    size: Int,
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = "Profile",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size.dp)
            .clip(shape = CircleShape)
    )
}