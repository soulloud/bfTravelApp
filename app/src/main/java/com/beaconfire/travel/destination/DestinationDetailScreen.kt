package com.beaconfire.travel.destination

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Review
import com.beaconfire.travel.trips.TripUiState
import com.beaconfire.travel.trips.TripsViewModel
import com.beaconfire.travel.ui.ReviewCard
import com.beaconfire.travel.ui.component.section.Section
import com.beaconfire.travel.ui.component.section.SectionScreen
import com.beaconfire.travel.utils.DestinationManager
import com.beaconfire.travel.utils.MockData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.roundToInt
import androidx.compose.material3.rememberModalBottomSheetState as rememberModalBottomSheetState1

@Composable
fun DestinationDetailScreen(
    tripsViewModel: TripsViewModel,
    onNavigate: (Navigation) -> Unit,
) {
    val destinationViewModel: DestinationViewModel =
        viewModel(factory = DestinationViewModel.Factory)
    val destination = DestinationManager.getInstance().destination
    val destinationUiModel by destinationViewModel.reviewUiModel.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val sections = listOf(
        Section(sectionHeader = {
            Text(
                "About",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }) {
            DestinationInfoCard(destination) {
                showSheet = true
            }
        },
        Section(sectionHeader = {
            Text(
                "Description",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }) { DescriptionCard(destination) },
        Section(sectionHeader = {
            Text(
                "Photos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }) { ImagePager(destination) },
        Section(sectionHeader = {
            Text(
                "Activities",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }) { ActivityCard() },
        Section(sectionHeader = {
            Text(
                "Reviews",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }) {
            RatingsAndReviewsScreen(
                destinationViewModel = destinationViewModel,
                destination = destination,
                reviews = destinationUiModel.reviews
            )
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 96.dp)
    ) {
        DestinationImageCard(DestinationManager.getInstance().destination, onNavigate)
        SectionScreen(
            title = "${destination.name} \u2708 ${destination.location}",
            sections = sections
        )
        if (showSheet) {
            AddToTripBottomSheet(
                tripsViewModel,
                DestinationManager.getInstance().destination,
                onNavigate
            ) { showSheet = false }
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
            .padding(bottom = 4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1.0f), horizontalAlignment = Alignment.Start) {
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
                }
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
            .padding(bottom = 4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = RectangleShape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
    val pagerState = rememberPagerState()

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        shape = RectangleShape
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(200.dp)
        ) {
            HorizontalPager(
                count = destination.images.size - 1,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(destination.images[it + 1])
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
            .padding(bottom = 4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
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
            .height(256.dp)
            .padding(bottom = 4.dp),
        shape = RoundedCornerShape(16.dp),
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
    destination: Destination,
    onNavigate: (Navigation) -> Unit
) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(destination.images.first())
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
    destination: Destination,
    onNavigate: (Navigation) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState1()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        val context = LocalContext.current
        val tripUiModel by tripsViewModel.tripUiModel.collectAsState()
        Column(modifier = Modifier.padding(32.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(1.0f), text = "Add Trip To:")
                IconButton(onClick = { onNavigate(Navigation.Trips) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Divider(modifier = Modifier.padding(4.dp))
            when (tripUiModel.tripUiState) {
                TripUiState.LoadSucceed -> {
                    LazyColumn {
                        itemsIndexed(tripUiModel.trips) { _, trip ->
                            val checked =
                                trip.destinations.any { it.destinationId == destination.destinationId }
                            Row(
                                modifier = Modifier.clickable {
                                    if (!checked) {
                                        tripsViewModel.addDestination(trip, destination)
                                    } else {
                                        tripsViewModel.removeDestination(trip, destination)
                                    }
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = {
                                        if (it) {
                                            tripsViewModel.addDestination(trip, destination)
                                        } else {
                                            tripsViewModel.removeDestination(trip, destination)
                                        }
                                    })
                                Text(text = trip.title)
                            }
                        }
                    }
                }

                else -> {}
            }
            Divider(modifier = Modifier.padding(4.dp))
            Button(onClick = onDismiss) {
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

@Composable
fun RatingsAndReviewsScreen(
    destinationViewModel: DestinationViewModel,
    destination: Destination,
    reviews: List<Review>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderSection(
            averageRating = reviews.map { it.score }.average(),
            totalRatings = reviews.size
        )
        Spacer(Modifier.height(16.dp))
        StarRatings(reviews)
        Spacer(Modifier.height(16.dp))
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                destinationViewModel.createNewReview(
                    Review(
                        reviewId = "",
                        destination = destination.destinationId ?: "",
                        score = 3.0,
                        title = "empty title",
                        description = "Nothing",
                        timestamp = "00:00",
                        owner = ""
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = "Write a Review",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        LazyRow {
            itemsIndexed(reviews) { _, review -> ReviewCard(review) }
        }
    }
}

@Composable
fun HeaderSection(averageRating: Double, totalRatings: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "%.1f".format(averageRating),
                style = MaterialTheme.typography.displayMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star",
                    tint = Color(0xFFFFD700)
                )
                Text(
                    text = "$totalRatings Ratings",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun StarRatings(reviews: List<Review>) {
    val scores = reviews.groupBy { it.score.roundToInt() }
    val max = scores.entries.maxOfOrNull { it.value.size } ?: 0
    Column {
        RatingRow(starCount = 5, ratingCount = scores[5]?.size ?: 0, maxCount = max)
        RatingRow(starCount = 4, ratingCount = scores[4]?.size ?: 0, maxCount = max)
        RatingRow(starCount = 3, ratingCount = scores[3]?.size ?: 0, maxCount = max)
        RatingRow(starCount = 2, ratingCount = scores[2]?.size ?: 0, maxCount = max)
        RatingRow(starCount = 1, ratingCount = scores[1]?.size ?: 0, maxCount = max)
        TextButton(onClick = { /* TODO: Terms and Conditions Action */ }) {
            Text(text = "Terms and Conditions", color = Color.Gray)
        }
    }
}

@Composable
fun RatingRow(starCount: Int, ratingCount: Int, maxCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$starCount Star", modifier = Modifier.width(64.dp))
        LinearProgressIndicator(
            progress = ratingCount / maxCount.toFloat(),
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .progressSemantics()
        )
    }
}

