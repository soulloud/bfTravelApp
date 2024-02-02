package com.beaconfire.travel.ui.component.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingPicker(
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    currentRating: Double = 1.0,
    onRatingChanged: (Double) -> Unit
) {
    var rating by remember { mutableStateOf(currentRating) }

    Row(modifier = modifier) {
        (1..maxRating).forEach { index ->
            val star = if (index <= rating) Icons.Filled.Star else Icons.Filled.StarBorder
            Icon(
                imageVector = star,
                contentDescription = "Rating",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        rating = index.toDouble()
                        onRatingChanged(rating)
                    },
                tint = if (index <= rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (index < maxRating) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}