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
fun List<Review>.sort(): List<Review> {
    val formatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT)
    return filter {
        try {
            LocalDateTime.parse(it.timestamp, formatter)
            true
        } catch (e: Exception) {
            false
        }
    }.sortedByDescending { LocalDateTime.parse(it.timestamp, formatter) }
}
