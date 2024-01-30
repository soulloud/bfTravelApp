package com.beaconfire.travel.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.beaconfire.travel.repo.model.User

@Composable
fun ProfileImage(
    user: User,
    image: Int,
    size: Int,
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = "Profile",
        modifier = Modifier
            .size(size.dp)
            .clip(shape = RoundedCornerShape(50.dp))
    )
}