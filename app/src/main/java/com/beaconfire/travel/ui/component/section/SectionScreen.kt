package com.beaconfire.travel.ui.component.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

/**
 *     val mockSections = listOf(
 *         Section(sectionHeader = { Text("helloworld") }) { Column { repeat(20) { Text(text = "1 -- $it --") } } },
 *         Section(sectionHeader = {
 *             Image(
 *                 painter = painterResource(id = R.drawable.ic_profile_alice),
 *                 contentDescription = null
 *             )
 *         }) { Column { repeat(20) { Text(text = "2 -- $it --") } } },
 *         Section(sectionHeader = { Text("Hi there") }) {
 *             Column {
 *                 repeat(20) { Text(text = "3 -- $it --") }
 *             }
 *             Image(painter = painterResource(id = R.drawable.ic_hawali), contentDescription = null)
 *         },
 *         Section(sectionHeader = { Text("4") }) { Column { repeat(20) { Text(text = "4 -- $it --") } } },
 *         Section(sectionHeader = {
 *             Image(
 *                 painter = painterResource(id = R.drawable.ic_profile_lili),
 *                 contentDescription = null
 *             )
 *         }) { Column { repeat(20) { Text(text = "5 -- $it --") } } },
 *     )
 *
 *     SectionScreen(title = "Title", sections = mockSections)
 */
@Composable
fun SectionScreen(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    sections: List<Section>
) {
    val scope = rememberCoroutineScope()

    val sectionsListState = rememberLazyListState()
    val itemsListState = rememberLazyListState()
    var selectedSectionIndex by remember { mutableIntStateOf(0) }

    Column(modifier) {
        title()

        SectionTitleViews(
            selectedIndex = selectedSectionIndex,
            sections = sections,
            sectionsListState = sectionsListState,
            onClick = { sectionIndex ->
                selectedSectionIndex = sectionIndex

                scope.launch {
                    sectionsListState.animateScrollToItem(sectionIndex)
                    itemsListState.animateScrollToItem(sectionIndex)
                }
            }
        )

        Divider()

        SectionItemViews(
            sections = sections,
            itemsListState = itemsListState,
            onPostScroll = {
                val currentSectionIndex = itemsListState.firstVisibleItemIndex
                if (selectedSectionIndex != currentSectionIndex) {
                    selectedSectionIndex = currentSectionIndex

                    scope.launch {
                        sectionsListState.animateScrollToItem(currentSectionIndex)
                    }
                }
            }
        )
    }
}
