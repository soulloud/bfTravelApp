@file:OptIn(ExperimentalPagerApi::class)

package com.beaconfire.travel.ui.component.carousel

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay

/**
 *     val images = listOf(
 *         "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
 *         "https://buffer.com/library/content/images/2023/10/free-images.jpg",
 *         "https://media.macphun.com/img/uploads/customer/how-to/608/15542038745ca344e267fb80.28757312.jpg?q=85&w=1340",
 *         "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg"
 *     )
 *     Card(
 *         modifier = Modifier.padding(16.dp),
 *         shape = RoundedCornerShape(16.dp),
 *     ) {
 *         AutoSlidingCarousel(
 *             itemsCount = images.size,
 *             itemContent = { index ->
 *                 AsyncImage(
 *                     model = ImageRequest.Builder(LocalContext.current)
 *                         .data(images[index])
 *                         .build(),
 *                     contentDescription = null,
 *                     contentScale = ContentScale.Crop,
 *                     modifier = Modifier.height(200.dp)
 *                 )
 *             }
 *         )
 *     }
 */
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 1000,
    pagerState: PagerState = remember { PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(pagerState.currentPage) {
        delay(autoSlideDuration)
        pagerState.animateScrollToPage((pagerState.currentPage + 1) % itemsCount)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(count = itemsCount, state = pagerState) { page ->
            itemContent(page)
        }

        // you can remove the surface in case you don't want 
        // the transparant bacground
        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 8.dp
            )
        }
    }
}
