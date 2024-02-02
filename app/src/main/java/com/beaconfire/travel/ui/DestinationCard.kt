package com.beaconfire.travel.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Review
import com.beaconfire.travel.utils.DestinationManager

@Composable
fun DestinationCard(
    destination: Destination,
    reviews: List<Review>,
    image: String,
    onNavigate: (Navigation) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable {
                DestinationManager
                    .getInstance()
                    .setDestination(destination)
                onNavigate(Navigation.DestinationDetail)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.onPrimary)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .build(),
                contentDescription = "Destination Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(196.dp),
                contentScale = ContentScale.FillWidth,
            )
            Row(modifier = Modifier.padding(4.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(destination.name, style = MaterialTheme.typography.titleMedium)
                    Text(destination.location, style = MaterialTheme.typography.bodyMedium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "%.1f".format(reviews.map { it.score }.average()),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Reviews",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(text = "${reviews.size}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
