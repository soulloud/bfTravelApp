package com.beaconfire.travel.ui.component.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
internal fun SectionTitleView(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    sectionTitle: @Composable () -> Unit,
) {
    Column(modifier.fillMaxWidth()) {
        var textWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        sectionTitle()

        //Show the text underline with animation
        AnimatedVisibility(
            visible = isSelected,
            enter = expandHorizontally() + fadeIn(),
            exit = shrinkHorizontally() + fadeOut()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                sectionTitle()
            }
        }
    }
}
