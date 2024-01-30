package com.beaconfire.travel.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beaconfire.travel.navigation.Navigation

@Composable
fun SearchScreen(onNavigate: (Navigation) -> Unit) {
    val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
    val searchUiModel = searchViewModel.searchUiModel
    Column {
        Button(onClick = { onNavigate(Navigation.Back) }) {
            Text(text = "Back")
        }
        Button(onClick = { searchViewModel.search("Chicago") }) {
            Text(text = "Search")
        }
        when (searchUiModel) {
            is SearchUiModel.Searching -> {
                Text(text = "Searching")
            }

            is SearchUiModel.SearchSucceed -> {
                LazyColumn {
                    itemsIndexed(searchUiModel.destinations) { _, destination ->
                        val str = "${destination.name}, ${destination.location}"
                        Text(str, modifier = Modifier.padding(bottom = 20.dp))
                    }
                }
            }

            is SearchUiModel.SearchFailed -> {}
            else -> {}
        }
    }
}
