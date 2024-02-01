package com.beaconfire.travel.utils

import com.beaconfire.travel.repo.model.Destination

enum class DestinationSort {
    AlphabetAscending,
    AlphabetDescending,
    None,
}

sealed class DestinationFilter {
    data class FilterByTag(val tags: List<String>) : DestinationFilter()
    data class FilterByName(val name: String) : DestinationFilter()
    data class FilterByLocation(val location: String) : DestinationFilter()
    data class FilterByNameOrLocation(val inputText: String) : DestinationFilter()
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

    is DestinationFilter.FilterByName -> this.filter { destination ->
        destination.name.contains(filter.name, ignoreCase = true)
    }

    is DestinationFilter.FilterByLocation -> this.filter { destination ->
        destination.location.contains(filter.location, ignoreCase = true)
    }

    is DestinationFilter.FilterByNameOrLocation -> this.filter { destination ->
        destination.name.contains(filter.inputText, ignoreCase = true)
                || destination.location.contains(filter.inputText, ignoreCase = true)
    }

    else -> this
}
