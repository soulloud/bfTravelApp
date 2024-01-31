package com.beaconfire.travel.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.beaconfire.travel.R
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.User
import com.beaconfire.travel.ui.TagCard
import com.beaconfire.travel.ui.component.carousel.AutoSlidingCarousel
import com.beaconfire.travel.utils.MockData
import com.beaconfire.travel.utils.SortMethod
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(onNavigate: (Navigation) -> Unit) {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val homeUiModel = homeViewModel.homeUiModel
    var tags = remember { mutableListOf<String>() }
    LazyColumn(Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 64.dp)) {
        item {
            Title(User.mockUsers[0])
            TopMenu(onNavigate, homeViewModel)
            LazyRow {
                items(MockData.tagImages.size) {
                    TagCard(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(156.dp)
                            .height(128.dp),
                        enabled = tags.contains(MockData.tags[it]),
                        image = MockData.tagImages[it],
                        tag = MockData.tags[it]
                    ) { enabled ->
                        if (enabled) {
                            tags.add(MockData.tags[it])
                        } else {
                            tags.remove(MockData.tags[it])
                        }
                        homeViewModel.onTagUpdated(tags)
                    }
                }
            }

            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                AutoSlidingCarousel(
                    itemsCount = MockData.destinationImages.size,
                    itemContent = { index ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(MockData.destinationImages[index])
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(200.dp)
                        )
                    }
                )
            }

            when (homeUiModel) {
                is HomeUiModel.Loading -> {
                    Text(text = "Loading")
                }

                is HomeUiModel.LoadSucceed -> {
//                LazyColumn {
//                    itemsIndexed(homeUiModel.destinations) { _, destination ->
//                        val str = "${destination.name}, ${destination.location}"
//                        Text(str, modifier = Modifier.padding(bottom = 20.dp))
//                    }
//                }
                }

                is HomeUiModel.LoadFailed -> {}
                else -> {}
            }
            Destinations(onNavigate)
        }
    }
}


@Composable
private fun TopMenu(
    onNavigate: (Navigation) -> Unit,
    homeViewModel: HomeViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(5f)) {
            DestinationSearchBar(onNavigate)
        }
        Row(modifier = Modifier.weight(1f)) {
            Actions(scope = rememberCoroutineScope(), homeViewModel = homeViewModel)
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

@Composable
fun Destinations(onNavigate: (Navigation) -> Unit) {
    val destinations = Destination.mockDestinations
    val images = Destination.mockImages

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(128.dp),
        modifier = Modifier
            .fillMaxSize()
            .height(640.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {
        items(destinations.size) {
            DestinationCard(destination = destinations[it], image = images[it], onNavigate = onNavigate)
        }
    }
}

@Composable
fun DestinationCard(
    destination: Destination,
    image: Int,
    onNavigate: (Navigation) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth().clickable { onNavigate(Navigation.DestinationDetail) },
    ) {
        Column {
            // Use Coil to load image from URL
            Image(
                painter = painterResource(id = image),
                contentDescription = "Destination Image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row(modifier = Modifier.padding(4.dp)) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(destination.name, style = MaterialTheme.typography.titleMedium)
                    Text(destination.location, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(modifier = Modifier.weight(1f)) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("4.8 (32) reviews)", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun Title(
    user: User
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_profile_shuaige),
            contentDescription = "Profile",
            modifier = Modifier
                .size(32.dp)
                .clip(shape = RoundedCornerShape(50.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text("Hello, ${user.displayName}")
    }
}

@Composable
private fun Actions(
    scope: CoroutineScope,
    homeViewModel: HomeViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = {
            expanded = true
        }) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "More Menu"
            )
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }) {
        FilterDropdownMenuItem(
            text = "A - Z",
            homeViewModel,
            SortMethod.AlphabetAscending
        ) { expanded = false }
        FilterDropdownMenuItem(
            text = "Z - A",
            homeViewModel,
            SortMethod.AlphabetDescending
        ) { expanded = false }
    }
}

@Composable
private fun FilterDropdownMenuItem(
    text: String,
    homeViewModel: HomeViewModel,
    sortMethod: SortMethod,
    onClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    DropdownMenuItem(
        text = { Text(text = text) },
        onClick = {
            scope.launch { }
            onClick()
        })
}
