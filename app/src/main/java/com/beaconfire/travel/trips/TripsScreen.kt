package com.beaconfire.travel.trips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beaconfire.travel.R
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Trip

@Composable
fun TripsScreen() {
    val tripsViewModel: TripsViewModel = viewModel(factory = TripsViewModel.Factory)
    val tripUiModel by tripsViewModel.tripUiModel.collectAsState()
    var showSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().paint(
            painter = painterResource(R.drawable.ic_background),
            contentScale = ContentScale.FillHeight)
    ) {
        LazyRow(modifier = Modifier
            .padding(8.dp)
            .weight(1.0f)) {
            items(tripUiModel.trips.size) {
                TripCard(trip = tripUiModel.trips[it],
                    {
                        tripsViewModel.removeDestination(
                            tripUiModel.trips[it],
                            tripUiModel.trips[it].destinations[it]
                        )
                    },
                    { tripsViewModel.toggleTripVisibility(tripUiModel.trips[it]) },
                    { tripsViewModel.deleteTrip(tripUiModel.trips[it]) })
            }
        }

        if (showSheet) {
            TripCreationContent { showSheet = false }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(end = 32.dp, bottom = 96.dp)
                .align(Alignment.End),
            onClick = {
                showSheet = true
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Localized description"
            )
        }
    }
}

@Composable
fun TripCard(
    trip: Trip,
    onClickDeleteDestination: () -> Unit,
    onClickChangeVisibility: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(320.dp)
            .height(480.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = trip.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Duration: ${trip.duration}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            trip.destinations.take(3).forEach {
                DestinationItemCard(it, onClick = onClickDeleteDestination)
            }
            Spacer(modifier = Modifier.height(4.dp))
            TripVisibilityCard(trip, onClickChangeVisibility)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Participants: ${trip.numPeople}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onClick) {
                Text(text = "Delete Trip")
            }
        }
    }
}

@Composable
fun DestinationItemCard(
    destination: Destination,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "Destination: ${destination.name} - ${destination.location}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = onClick, modifier = Modifier.padding(end = 8.dp)) {
            Text(text = "Delete")
        }
        Divider()
    }
}

@Composable
fun TripVisibilityCard(
    trip: Trip,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "Visibility: ${if (trip.visibility.value.contains("private")) "private" else "public"}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = onClick, modifier = Modifier.padding(end = 8.dp)) {
            Text(text = "Change")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripCreationContent(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = TextFieldValue("Itinerary"),
                onValueChange = { /* Handle change */ },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.CalendarToday, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DateField("Starts")
                DateField("Ends")
            }
        }
    }
}

@Composable
fun DateField(label: String) {
    OutlinedButton(
        onClick = { /* Open date picker */ },
        border = BorderStroke(1.dp, Color.White),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
    ) {
        Text(label)
    }
}
