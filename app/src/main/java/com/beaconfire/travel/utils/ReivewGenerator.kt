package com.beaconfire.travel.utils

import com.beaconfire.travel.repo.model.Review
import kotlin.random.Random
import kotlin.random.nextInt

class ReviewGenerator() {

    val positveTitleList = listOf<String>(
        "Best place",
        "Good vacation destination",
        "Budget Friendly for Students",
        "Hidden Gem: A Must-Visit Paradise",
        "Unforgettable Getaway: Top Vacation Destination",
        "Culinary Delight: Exceptional Dining Experience",
        "Budget-Friendly Bliss: Ideal for Students",
        "Serenity by the Shore: A Beach Lover's Dream",
        "Incredible Views: The Perfect Retreat",
        "Exceptional Eats: A Foodie's Haven",
        "Affordable Luxury: Student-Friendly Hotspot",
        "Scenic Tranquility: Your Next Best Escape",
        "Sun, Sand, and Smiles: Beach Perfection"
    )

    val negativeTitleList = listOf<String>(
        "Avoid at All Costs: A Nightmare Destination",
        "Disappointing Getaway: Not Worth Your Time",
        "Culinary Letdown: Subpar Dining Experience",
        "Overpriced and Underwhelming: Not for Students",
        "Shoreline Disappointment: Skip This Beach",
        "Views to Forget: The Worst Retreat",
        "Dining Disaster: A Foodie's Regrettable Choice",
        "Budget Breaker: Not Student-Friendly",
        "Tranquility Turned Turmoil: A Regrettable Escape",
        "Beach Blunder: Stay Away from This Spot"
    )

    val timeStampList = listOf<String>(
        "2022-03-01T12:34:56",
        "2022-04-05T18:45:30",
        "2022-05-10T08:15:22",
        "2022-06-15T14:55:48",
        "2022-07-20T20:25:11",
        "2022-08-25T05:40:09",
        "2022-09-30T16:12:37",
        "2023-01-04T11:27:44",
        "2023-03-09T22:09:18",
        "2023-05-14T09:50:02",
        "2023-07-19T13:58:27",
        "2023-09-24T17:21:03",
        "2023-11-29T23:05:45",
    )

    fun getRandomPositiveReview(destination: String, owner: String): Review{
        return Review(
            reviewId = "",
            destination = destination,
            score = Random.nextInt(4..5).toDouble(),
            title = positveTitleList[Random.nextInt(0..9)],
            description = positveTitleList[Random.nextInt(0..9)],
            timestamp = timeStampList[Random.nextInt(0..12)],
            owner = owner
        )
    }

    fun getRandomNegativeReview(destination: String, owner: String): Review{
        return Review(
            reviewId = "",
            destination = destination,
            score = Random.nextInt(1..3).toDouble(),
            title = negativeTitleList[Random.nextInt(0..9)],
            description = negativeTitleList[Random.nextInt(0..9)],
            timestamp = timeStampList[Random.nextInt(0..12)],
            owner = owner
        )
    }
}