package com.beaconfire.travel.utils

import com.beaconfire.travel.R
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Price
import com.beaconfire.travel.repo.model.Review

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

    val profileIcons = listOf(
        R.drawable.ic_profile_alice,
        R.drawable.ic_profile_mia,
        R.drawable.ic_profile_mike,
        R.drawable.ic_profile_shali,
        R.drawable.ic_profile_shuaige
    )


    val destination = Destination(
        name = "Lorem ipsum",
        location = "China",
        description = "Integer placerat dui odio, quis tempus libero mollis molestie. Curabitur ultrices leo diam, at porta massa suscipit sagittis. Vivamus a nisi quam. Aliquam sit amet lobortis sapien, vitae porttitor magna. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vestibulum sit amet feugiat nisl. Nunc mattis odio sit amet erat mollis, vitae tempus nibh porttitor. Nunc ac imperdiet nunc. Proin eget laoreet arcu. Mauris fringilla vehicula mauris, non viverra ex pharetra ut. Suspendisse at dui nec nisi scelerisque convallis at dignissim risus. Curabitur rhoncus diam vitae libero auctor, quis feugiat neque ornare. Donec id eros turpis. In hac habitasse platea dictumst. Duis sit amet metus ac dui suscipit vulputate sit amet in urna. Nam vel augue faucibus, faucibus ligula non, sollicitudin risus.\n" +
                "\n" +
                "Cras sollicitudin, arcu vel luctus auctor, velit massa consectetur felis, non pellentesque enim felis gravida leo. Integer urna ipsum, pellentesque id nisi ac, cursus egestas mauris. Ut aliquet consectetur leo, id fermentum dolor pellentesque eleifend. Nulla neque eros, hendrerit in rhoncus eu, viverra sit amet est. Fusce lacinia erat a nulla sodales dignissim. Fusce viverra ligula ac egestas fringilla. Praesent aliquam elementum laoreet. Sed egestas dictum lacus vel tincidunt. In posuere a turpis eu iaculis.",
        images = listOf(
            "https://img.freepik.com/premium-photo/dock-with-mountain-background_865967-28763.jpg",
            "https://wallpapers.com/images/featured/beautiful-love-2wtfr7zi86cfovd9.jpg",
            "https://static8.depositphotos.com/1491329/1068/i/450/depositphotos_10687188-stock-photo-foggy-landscape-early-morning-mist.jpg",
            "https://www.womansworld.com/wp-content/uploads/2024/08/cute-cats.jpg?w=953",
            "https://hips.hearstapps.com/hmg-prod/images/cat-instagram-captions-64ff2dfa47e9a.jpg?crop=1xw:0.84375xh;center,top&resize=1200:*"
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

    val reviews = listOf(
        Review(
            "",
            destination = "destination1",
            description = "description1",
            score = 5.0,
            title = "title1",
            timestamp = "2024-02-01",
            owner = "owner"
        ),
        Review(
            "",
            destination = "destination2",
            description = "description2",
            score = 2.0,
            title = "title2",
            timestamp = "2024-03-02",
            owner = "owner"
        ),
        Review(
            "",
            destination = "destination3",
            description = "description3",
            score = 4.0,
            title = "title3",
            timestamp = "2024-04-03",
            owner = "owner"
        ),
    )
}
