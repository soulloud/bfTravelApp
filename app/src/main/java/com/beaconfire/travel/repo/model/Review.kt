package com.beaconfire.travel.repo.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.beaconfire.travel.constant.Constant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Review(
    val reviewId: String,
    val destination: String,
    val score: Double,
    val title: String,
    val description: String,
    val timestamp: String,
    val owner: String
)

@RequiresApi(Build.VERSION_CODES.O)
fun Review.getDateTime() = try {
    LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern(Constant.DATE_FORMAT))
} catch (e: Exception) {
    null
}

enum class ReviewSort {
    Newest,
    Oldest,
    Highest,
    Lowest,
    None,
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<Review>.sort(sort: ReviewSort = ReviewSort.Newest) = when (sort) {
    ReviewSort.Newest -> this.filter { it.getDateTime() != null }
        .sortedByDescending { it.getDateTime()!! }

    ReviewSort.Oldest -> this.filter { it.getDateTime() != null }.sortedBy { it.getDateTime()!! }
    ReviewSort.Highest -> this.filter { it.getDateTime() != null }
        .sortedByDescending { it.getDateTime()!! }.sortedByDescending { it.score }

    ReviewSort.Lowest -> this.filter { it.getDateTime() != null }
        .sortedByDescending { it.getDateTime()!! }.sortedBy { it.score }

    else -> this
}
