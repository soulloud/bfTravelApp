package com.beaconfire.travel.repo.data

import com.beaconfire.travel.repo.model.Review

data class ReviewData(
    val reviewId: String? = null,
    val destination: String? = null,
    val score: Double? = null,
    val title: String? = null,
    val description: String? = null,
    val timestamp: String? = null,
    val owner: String? = null
) {
    fun toReview() = Review(
        reviewId = reviewId!!,
        destination = destination!!,
        score = score ?: 0.0,
        title = title ?: "empty title",
        description = description ?: "Nothing",
        timestamp = timestamp ?: "00:00",
        owner = owner!!
    )
}
