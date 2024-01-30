package com.beaconfire.travel.ui.component.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SectionTitleViews(
    selectedIndex: Int,
    sections: List<Section>,
    sectionsListState: LazyListState,
    onClick: (sectionIndex: Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(),
        state = sectionsListState
    ) {
        sections.forEachIndexed { i, section ->
            item {
                SectionTitleView(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickable { onClick(i) },
                    isSelected = selectedIndex == i,
                    sectionTitle = section.sectionHeader
                )
            }
        }
    }
}
