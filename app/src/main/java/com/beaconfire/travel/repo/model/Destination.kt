package com.beaconfire.travel.repo.model

import com.beaconfire.travel.R

data class Destination(
    val name: String = "",
    val ownerOrOrganization: String = "",
    val location: String = "",
    val description: String = "",
    val reviewList: List<String>? = emptyList(), // Assuming reviews are represented as strings
    val price: Price = Price.InvalidPrice,
    val localLanguages: List<String> = emptyList(),
    val ageRecommendation: String = "",
    val recommendedByPercentage: String = "",
) {
    companion object {
        val mockDestinations = listOf(
            Destination(
                name = "LA",
                ownerOrOrganization = "East Java Park Services",
                location = "Lumajang, Java Timur",
                description = "A scenic lake in the midst of the Tengger Semeru National Park, offering picturesque views and camping sites.",
                reviewList = listOf(
                    "Absolutely stunning at sunrise!",
                    "The hike is challenging but the view is worth it.",
                    "Don't forget to bring warm clothes, it gets chilly at night."
                ),
                price = Price(198.0, "USD"),
                localLanguages = listOf("Indonesian", "Javanese"),
                ageRecommendation = "Suitable for ages 12 and up due to the hiking difficulty.",
                recommendedByPercentage = "94%"
            ),
            Destination(
                name = "Las Vegas",
                ownerOrOrganization = "Bali Beach Authority",
                location = "Kuta, Bali",
                description = "A hidden beach located behind a high cliff with access to bright white sand and calm blue waters.",
                reviewList = listOf(
                    "A tranquil beach, less crowded than others in Bali.",
                    "Great for water sports and sunbathing!",
                    "The local food stalls offer some amazing treats."
                ),
                price = Price(8.0, "USD"),
                localLanguages = listOf("Indonesian", "Balinese"),
                ageRecommendation = "All ages",
                recommendedByPercentage = "88%"
            ),
            Destination(
                name = "Yellow Stone",
                ownerOrOrganization = "Cultural Heritage Preservation",
                location = "Magelang, Central Java",
                description = "The world's largest Buddhist temple, an archaeological site known for its multiple stacked platforms adorned with intricate carvings.",
                reviewList = listOf(
                    "The monument is a marvel, rich with history.",
                    "Go early to avoid the crowds and the heat.",
                    "A must-visit for anyone interested in culture and architecture."
                ),
                price = Price(198.0, "USD"),
                localLanguages = listOf("Indonesian"),
                ageRecommendation = "All ages",
                recommendedByPercentage = "97%"
            ),
            Destination(
                name = "Yosemite",
                ownerOrOrganization = "East Java Natural Wonders",
                location = "Probolinggo, East Java",
                description = "An active volcano well-known for its majestic views, particularly the sunrise from the crater rim.",
                reviewList = listOf(
                    "The sunrise view is otherworldly.",
                    "Quite crowded, but there are many vantage points for photos.",
                    "The horse ride to the crater was an unforgettable experience."
                ),
                price = Price(398.0, "USD"),
                localLanguages = listOf("Indonesian", "Javanese"),
                ageRecommendation = "Suitable for ages 10 and up.",
                recommendedByPercentage = "90%"
            ),
            Destination(
                name = "Hawaii",
                ownerOrOrganization = "Ubud Sanctuary Management",
                location = "Ubud, Bali",
                description = "A nature reserve and Hindu temple complex that is home to a troop of grey long-tailed macaques.",
                reviewList = listOf(
                    "The monkeys are adorable but can be mischievous; secure your belongings!",
                    "Lovely walk through the forest, very peaceful and cool.",
                    "The temples inside the forest are quite fascinating."
                ),
                price = Price(99.0, "USD"),
                localLanguages = listOf("Indonesian", "Balinese"),
                ageRecommendation = "All ages",
                recommendedByPercentage = "85%"
            )
        )

        val mockImages = listOf(
            R.drawable.ic_la,
            R.drawable.ic_las_vegas,
            R.drawable.ic_yellow_stone,
            R.drawable.ic_yosemite,
            R.drawable.ic_hawali,
        )
    }
}
