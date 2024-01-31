package com.beaconfire.travel.trips

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TripsScreen() {
    val tripsViewModel: TripsViewModel = viewModel(factory = TripsViewModel.Factory)
    //val destinationUiState = tripsViewModel.destinationListUiState
    val totalCost by tripsViewModel.totalCost.observeAsState()
    val tripUiState = tripsViewModel.tripUiState


//    // test for create new trip
//    tripsViewModel.createNewTrip(
//        TripData(
//            owner = tripsViewModel.huichangId,
//            title = "Go Japan"
//            )
//    )

//    // test for delete trip
//    if (tripUiState is TripUiState.LoadSucceed){
//        for (trip in tripUiState.trips){
//            if (trip.title.equals( "Go Japan")){
//                tripsViewModel.deleteCurrentTrip(trip)
//            }
//        }
//    }

    // test for change visibility

    //test for load trip
    if (tripUiState is TripUiState.LoadSucceed){
        Text(text = tripUiState.trips.toString(), modifier = Modifier.padding(all = 30.dp))

        tripsViewModel.setCurrentDestinationList(tripUiState.trips[0])
        tripsViewModel.changeTripVisibility(tripUiState.trips[0])


//        tripsViewModel.removeDestinationFromCurrentTrip(Destination(), tripUiState.trips[0])

        Text(text = totalCost.toString())
    }

//    if (destinationUiState is DestinationUiState.LoadSucceed){
//        LazyColumn {
//            itemsIndexed(destinationUiState.destinations) { _, destination ->
//                val str = "${destination.name}, ${destination.location}"
//                Text(str, modifier = Modifier.padding(bottom = 20.dp))
//            }
//        }
//    }


}
