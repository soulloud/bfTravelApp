package com.beaconfire.travel.destination

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.beaconfire.travel.navigation.Navigation

@Composable
fun DestinationDetailScreen(onNavigate: (Navigation) -> Unit) {
    Column {
        Button(onClick = { onNavigate(Navigation.Back) }) {
            Text(text = "Back")
        }
        Text(text = "DestinationDetailScreen")
    }
}
