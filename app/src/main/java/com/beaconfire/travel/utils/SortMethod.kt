package com.beaconfire.travel.utils

import com.beaconfire.travel.repo.model.Destination

sealed class SortMethod {
    data object AlphabetAscending : SortMethod()
    data object AlphabetDescending : SortMethod()

}

fun List<Destination>.sort(filterMode: SortMethod) = when (filterMode) {
    SortMethod.AlphabetAscending -> this.sortedBy { it.name }
    SortMethod.AlphabetDescending -> this.sortedByDescending { it.name }
    else -> this
}
