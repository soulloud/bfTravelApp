package com.beaconfire.travel.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beaconfire.travel.repo.model.Review

@Composable
fun ReviewCard(
    review: Review
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                ReviewDetails(review = review)
                //CartActionBox(cartViewModel, cartEntity)
            }
        }
    }
}

@Composable
fun ReviewDetails(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = review.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
//        Text(
//            text = "${review.score.toString()}: by ${review.timestamp}",
//            fontSize = 10.sp,
//            color = MaterialTheme.colorScheme.primary
//        )
        Text(
            text = review.description,
            fontSize = 14.sp
        )
        Text(
            text = "Reviewed ${review.timestamp.substring(0, 10)}",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}