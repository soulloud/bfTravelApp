package com.beaconfire.travel.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beaconfire.travel.navigation.Navigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(onNavigate: (Navigation) -> Unit) {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val homeUiModel = homeViewModel.homeUiModel
    Column(Modifier.padding(8.dp)) {
        DestinationSearchBar(onNavigate)
        when (homeUiModel) {
            is HomeUiModel.Loading -> {
                Text(text = "Loading")
            }

            is HomeUiModel.LoadSucceed -> {
                LazyColumn {
                    itemsIndexed(homeUiModel.destinations) { _, destination ->
                        val str = "${destination.name}, ${destination.location}"
                        Text(str, modifier = Modifier.padding(bottom = 20.dp))
                    }
                }
            }

            is HomeUiModel.LoadFailed -> {}
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationSearchBar(onNavigate: (Navigation) -> Unit) {
    SearchBar(
        query = "Search for Destination",//text showed on SearchBar
        onQueryChange = {}, //update the value of searchText
        onSearch = { onNavigate(Navigation.Search) },
        active = false, //whether the user is searching or not
        onActiveChange = { onNavigate(Navigation.Search) }, //the callback to be invoked when this search bar's active state is changed
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        trailingIcon = {
            IconButton(
                onClick = { onNavigate(Navigation.Search) },
                modifier = Modifier.size(50.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    ) {}
}
