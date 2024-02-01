package com.beaconfire.travel.utils

import com.beaconfire.travel.R
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Price
import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.repo.model.User

object MockData {
    val destinationImages = listOf(
        "https://media.vietravel.com/images/news/beijing_1.jpg",
        "https://www.studentuniverse.com/blog/wp-content/uploads/2014/04/Santorini-Greece.jpg",
        "https://i.ytimg.com/vi/uavcLD39m5Y/maxresdefault.jpg",
        "https://www.travelandleisure.com/thmb/wsA6EXFuYkqtuJGLbQWw05-cwPs=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/lake-como-MOSTBEAUTIFUL0921-cb08f3beff1041e4beefc67b5e956b23.jpg",
        "https://www.edreams.com/blog/wp-content/uploads/sites/3/2013/07/shutterstock_1293922393.jpg",
        "https://www.intrepidtravel.com/adventures/wp-content/uploads/2017/08/Intrepid-Travel-china_shanghai_yuyuan-garden-city.jpg",
        "https://www.flamingotravels.co.in/blog/wp-content/uploads/2019/05/China-feature-img.jpg",
        "https://images.ctfassets.net/rxqefefl3t5b/3S46ep6pvHbsjBaTwiZrwL/a413aaacdbf96f96fc982624efd3584f/shutterstock_351311348.jpg?fl=progressive&q=80",
    )

    val tagImages = listOf(
        "https://i0.wp.com/picjumbo.com/wp-content/uploads/magical-spring-forest-scenery-during-morning-breeze-free-photo.jpg?w=600&quality=80",
        "https://www.planetizen.com/files/images/Historic-Penn-Station.jpg",
        "https://wander-lush.org/wp-content/uploads/2020/12/Cultural-tourism-destinations-Mauritius-CanvaPro.jpg",
        "https://www.musicnotes.com/blog/content/images/now/wp-content/uploads/musical_destinations-min.jpg",
    )

    val tags = listOf(
        "Nature",
        "Historic",
        "Culture",
        "Music",
    )

    val users = listOf(
        User(
            userId = "",
            displayName = "John Doe",
            email = "john@example.com",
            password = "password123",
            currency = "USD",
            reviews = listOf("Review 1", "Review 2"),
            trips = listOf("Trip 1", "Trip 2"),
            saves = listOf("Save 1", "Save 2"),
            profile = Profile(),
        ),
        User(
            userId = "",
            displayName = "David",
            email = "john@example.com",
            password = "password123",
            currency = "USD",
            reviews = listOf("Review 1", "Review 2"),
            trips = listOf("Trip 1", "Trip 2"),
            saves = listOf("Save 1", "Save 2"),
            profile = Profile(),
        ),
        User(
            userId = "",
            displayName = "Su",
            email = "john@example.com",
            password = "password123",
            currency = "USD",
            reviews = listOf("Review 1", "Review 2"),
            trips = listOf("Trip 1", "Trip 2"),
            saves = listOf("Save 1", "Save 2"),
            profile = Profile(),
        ),
        User(
            userId = "",
            displayName = "Alice Smith",
            email = "alice@example.com",
            password = "alice_password",
            currency = "EUR",
            reviews = listOf("Review 3", "Review 4"),
            trips = listOf("Trip 3", "Trip 4"),
            saves = listOf("Save 3", "Save 4"),
            profile = Profile(),
        ),
        User(
            userId = "",
            displayName = "Bob Johnson",
            email = "bob@example.com",
            password = "bob_password",
            currency = "GBP",
            reviews = listOf("Review 5", "Review 6"),
            trips = listOf("Trip 5", "Trip 6"),
            saves = listOf("Save 5", "Save 6"),
            profile = Profile(),
        )
    )

    val profileIcons = listOf(
        R.drawable.ic_profile_alice,
        R.drawable.ic_profile_mia,
        R.drawable.ic_profile_mike,
        R.drawable.ic_profile_shali,
        R.drawable.ic_profile_shuaige
    )


    val destination = Destination(
        name = "beijing",
        location = "China",
        description = "Beijing, the capital city of China, is a vibrant metropolis rich in history and culture. Known for its ancient architecture, including the iconic Great Wall and the Forbidden City, Beijing offers a unique blend of traditional charm and modern dynamism. The city's hutongs, traditional alleyways, provide a glimpse into the old way of life, while the bustling shopping districts like Wangfujing showcase contemporary China. Culinary enthusiasts will find Beijing's food scene, especially the famous Peking duck, a delightful experience. The city's parks and temples offer serene retreats, making it a versatile destination for travelers.",
        images = listOf(
            "https://pyt-blogs.imgix.net/2016/02/12.jpg?auto=format&ixlib=php-3.3.0",
            "https://www.beijing-visitor.com/images/content_images/chinesenewyear2019.jpg"
        ),
        price = Price(233.0, "USD"),
        reviews = listOf("this,", "good"),
        destinationId = "3333",
        tags = listOf("nature", "music"),
        rating = 34.9
    )

    val categoryCard = listOf(
        listOf(
            "Camping & Hiking",
            R.drawable.ic_camp,
            "Money can not buy happiness but it can buy a tent and that's kinda the same thing."
        ),
        listOf(
            "Restaurant & Coffee",
            R.drawable.ic_food,
            "Know the best places to eat and drink in this City. "
        ),
        listOf("National Park", R.drawable.ic_park, "Best National Parks In this city To Visit "),
        listOf(
            "Buildings",
            R.drawable.ic_building,
            "Explore those beautiful buildings in this city"
        ),
    )
}
