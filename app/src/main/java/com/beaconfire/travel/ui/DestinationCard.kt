package com.beaconfire.travel.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.model.Destination

@Composable
fun DestinationCard(
    destination: Destination,
    image: String,
    onNavigate: (Navigation) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onNavigate(Navigation.DestinationDetail) },
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
            Row(
                modifier = Modifier
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(destination.name, style = MaterialTheme.typography.titleMedium)
                    Text(destination.location, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(modifier = Modifier.weight(1f)) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "${destination.rating}\n (${destination.reviews.size}) reviews",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
