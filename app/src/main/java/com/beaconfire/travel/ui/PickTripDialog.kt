package com.beaconfire.travel.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip

@Composable
fun PickTripDialog(
    trips: List<Trip>,
    destination: Destination
) {
    var showDialog by remember { mutableStateOf(true) }
    var selectedTrip by remember { mutableStateOf<Trip?>(null) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Adding to which trip?") },
            text = {
                Column {
                    trips.forEach { trip ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (trip == selectedTrip),
                                    onClick = { selectedTrip = trip }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (trip == selectedTrip),
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = trip.title)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        /* TODO */
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}