package com.beaconfire.travel.trips

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TripsScreen() {
    val tripsViewModel: TripsViewModel = viewModel(factory = TripsViewModel.Factory)
    val totalCost by tripsViewModel.totalCost.observeAsState()

}
