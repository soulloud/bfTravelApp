package com.beaconfire.travel.repo.data

import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Price

data class DestinationData(
    val destinationId: String? = null,
    val name: String? = null,
    val location: String? = null,
    val description: String? = null,
    val reviews: List<String>? = null,
    val price: Map<String, Any>? = emptyMap(),
    val rating: Double = 0.0,
    val tags: List<String> = emptyList(),
    val images: List<String> = emptyList(),
) {
    fun toDestination() = Destination(
        destinationId = destinationId,
        name = name ?: "",
        location = location ?: "",
        description = description ?: "",
        reviews = reviews ?: emptyList(),
        price = price?.toPrice() ?: Price.InvalidPrice,
        rating = rating,
        tags = tags,
        images = images,
    )
}

private fun Map<String, Any>.toPrice(): Price {
    val currency = get("currency") as String
    when (val value = get("value")) {
        is Long -> {
            return Price(value = value.toDouble(), currency = currency)
        }

        is Double -> {
            return Price(value = value, currency = currency)
        }

        is String -> {
            return Price(value = value.toDouble(), currency = currency)
        }

        else -> {
            return Price(value = 0.0, currency = "USD")
        }
    }
}
