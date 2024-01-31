package com.beaconfire.travel.utils

import com.beaconfire.travel.repo.model.Destination

enum class DestinationSort {
    AlphabetAscending,
    AlphabetDescending,
    None,
}

sealed class DestinationFilter {
    data class FilterByTag(val tags: List<String>) : DestinationFilter()
}

fun List<Destination>.sort(sortMode: DestinationSort) = when (sortMode) {
    DestinationSort.AlphabetAscending -> this.sortedBy { it.name }
    DestinationSort.AlphabetDescending -> this.sortedByDescending { it.name }
    else -> this
}

fun List<Destination>.filterBy(filter: DestinationFilter) = when (filter) {
    is DestinationFilter.FilterByTag -> this.filter { destination ->
        destination.tags.map { it.lowercase() }.containsAll(filter.tags.map { it.lowercase() })
    }

    else -> this
}
