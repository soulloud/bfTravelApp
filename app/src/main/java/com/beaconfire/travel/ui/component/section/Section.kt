package com.beaconfire.travel.ui.component.section

import androidx.compose.runtime.Composable

data class Section(val sectionHeader: @Composable () -> Unit, val content: @Composable () -> Unit)
