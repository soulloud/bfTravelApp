package com.beaconfire.travel.destination

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.trips.TripUiState
import com.beaconfire.travel.trips.TripsViewModel
import com.beaconfire.travel.utils.MockData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.material3.rememberModalBottomSheetState as rememberModalBottomSheetState1

@Composable
fun DestinationDetailScreen(
    tripsViewModel: TripsViewModel,
    onNavigate: (Navigation) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 96.dp)
    ) {
        item {
            DestinationImageCard(onNavigate)
            DestinationInfoCard(MockData.destination) { showSheet = true }
            DescriptionCard(MockData.destination)
            ImagePager(MockData.destination)
            ActivityCard()
            if (showSheet) {
                AddToTripBottomSheet(tripsViewModel) { showSheet = false }
            }
        }
    }

}

@Composable
fun DestinationInfoCard(
    destination: Destination,
    onAddToTrip: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Column {
                Text(
                    text = destination.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.GpsFixed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = destination.location,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Column(modifier = Modifier.weight(1.0f), horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${destination.price.value}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Per person",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = onAddToTrip) {
                    Text(text = "Add to Trip")
                }
            }
        }
    }
}

@Composable
fun DescriptionCard(
    destination: Destination
) {
    var expanded by remember { mutableStateOf(false) }
    val annotatedText = if (expanded) AnnotatedString("Read Less") else AnnotatedString("Read More")
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RectangleShape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = destination.description,
                style = MaterialTheme.typography.bodyMedium,

                maxLines = if (expanded) Int.MAX_VALUE else 4
            )
            ClickableText(
                text = annotatedText,
                onClick = { expanded = !expanded },
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
            )

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImagePager(destination: Destination) {
    // Remember a PagerState
    val pagerState = rememberPagerState()

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RectangleShape
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(200.dp)
        ) {
            HorizontalPager(
                count = destination.images.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(destination.images[it])
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.3f), // You can set the aspect ratio you need for your images
                        contentScale = ContentScale.Crop
                    )
                }

            }
        }
    }
}

@Composable
fun ActivityCard() {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "EXPERIENCE HERE, THE LAND OF ENCHANTMENT",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Integer placerat dui odio, quis tempus libero mollis molestie. Curabitur ultrices leo diam, at porta massa suscipit sagittis. Vivamus a nisi quam. Aliquam sit amet lobortis sapien, vitae porttitor magna.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            // This would be a LazyRow in a real application
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(MockData.categoryCard.size) {
                    CategoryCard(
                        title = MockData.categoryCard[it][0].toString(),
                        imageResId = MockData.categoryCard[it][1].toString().toInt(),
                        description = MockData.categoryCard[it][2].toString()
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(title: String, description: String, imageResId: Int) {
    Card(
        modifier = Modifier
            .width(192.dp)
            .height(256.dp),

        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = 0.25f
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .align(Alignment.BottomEnd),
                maxLines = 3,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun DestinationImageCard(
    onNavigate: (Navigation) -> Unit
) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://static.independent.co.uk/s3fs-public/thumbnails/image/2014/03/25/12/eiffel.jpg")
                .build(),
            contentDescription = "Destination Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp),
            contentScale = ContentScale.FillWidth,
        )
        IconButton(onClick = { onNavigate(Navigation.Back) }) {
            Icon(
                Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToTripBottomSheet(
    tripsViewModel: TripsViewModel,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState1()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        val tripUiState = tripsViewModel.tripUiState
        Column(modifier = Modifier.padding(32.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(1.0f), text = "Add Trip To:")
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Divider(modifier = Modifier.padding(4.dp))
            when (tripUiState) {
                is TripUiState.LoadSucceed -> {
                    LazyColumn {
                        itemsIndexed(tripUiState.trips) { _, trip ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = false, onCheckedChange = {})
                                Text(text = trip.title)
                            }
                        }
                    }
                }

                else -> {}
            }
            Divider(modifier = Modifier.padding(4.dp))
            Button(onClick = { /*TODO*/ }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(modifier = Modifier.fillMaxWidth(), text = "Done")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
