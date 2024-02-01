package com.beaconfire.travel.trips

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TripsScreen() {
    val tripsViewModel: TripsViewModel = viewModel(factory = TripsViewModel.Factory)
    //val destinationUiState = tripsViewModel.destinationListUiState
    val totalCost by tripsViewModel.totalCost.observeAsState()
    val tripUiState = tripsViewModel.tripUiState

    val testReviewRepoOutput by tripsViewModel.testReviewRepoOutput.observeAsState()



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
    if (tripUiState is TripUiState.LoadSucceed) {

    //Text(text = tripUiState.trips.toString(), modifier = Modifier.padding(all = 30.dp))
        //tripsViewModel.setCurrentDestinationList(tripUiState.trips[0]) //Ok
        //tripsViewModel.removeDestination(tripUiState.trips[0], tripsViewModel.currentDestinationList[0])

        //tripsViewModel.toggleTripVisibility(tripUiState.trips[0]) //OK

//        tripsViewModel.removeDestinationFromCurrentTrip(Destination(), tripUiState.trips[0])

        //Text(text = totalCost.toString())

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
