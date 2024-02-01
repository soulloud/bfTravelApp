package com.beaconfire.travel.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.ui.BlueIconButton
import com.beaconfire.travel.ui.DestinationCard
import com.beaconfire.travel.utils.DestinationSort
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onNavigate: (Navigation) -> Unit) {
    val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
    val searchUiModel by searchViewModel.searchUiModel.collectAsState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 96.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Navigation.Back) }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SearchBar(
                query = query,
                onQueryChanged = {
                    query = it
                    searchViewModel.search(query)
                },
                onSearch = {
                    searchViewModel.search(query)
                },
                searchViewModel = searchViewModel
            )

            when (searchUiModel.searchUiState) {
                SearchUiState.Searching -> {
                    Text(text = "Searching")
                }

                SearchUiState.SearchSucceed -> {
                    LazyColumn {
                        itemsIndexed(searchUiModel.destinations) { _, destination ->
                            DestinationCard(
                                destination = destination,
                                image = destination.images[2],
                                onNavigate = onNavigate
                            )
                        }
                    }
                }

                SearchUiState.SearchFailed -> {}

                else -> {}
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchViewModel: SearchViewModel,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = modifier,
        shadowElevation = 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(3f)) {

                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch(query)
                            keyboardController?.hide()
                        }
                    ),
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                Actions(searchViewModel = searchViewModel)
            }
        }
    }
}

@Composable
private fun Actions(
    searchViewModel: SearchViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {

        BlueIconButton(
            onClick = { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            FilterDropdownMenuItem(
                text = "A - Z",
                searchViewModel,
                DestinationSort.AlphabetAscending
            ) { expanded = false }
            FilterDropdownMenuItem(
                text = "Z - A",
                searchViewModel,
                DestinationSort.AlphabetDescending
            ) { expanded = false }
        }
    }
}

@Composable
private fun FilterDropdownMenuItem(
    text: String,
    searchViewModel: SearchViewModel,
    destinationSort: DestinationSort,
    onClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    DropdownMenuItem(
        text = { Text(text = text) },
        onClick = {
            scope.launch {
                searchViewModel.onSortChanged(destinationSort)
            }
            onClick()
        }
    )
}
