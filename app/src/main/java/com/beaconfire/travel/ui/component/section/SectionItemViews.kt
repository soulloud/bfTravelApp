package com.beaconfire.travel.ui.component.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll

@Composable
fun SectionItemViews(
    sections: List<Section>,
    itemsListState: LazyListState,
    onPostScroll: () -> Unit
) {
    LazyColumn(
        state = itemsListState,
        modifier = Modifier
            .padding()
            .nestedScroll(object : NestedScrollConnection {
                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    onPostScroll()
                    return super.onPostScroll(consumed, available, source)
                }
            })
    ) {
        sections.forEach { section ->
            item {
                section.sectionHeader()
                section.content()
            }
        }
    }
}
