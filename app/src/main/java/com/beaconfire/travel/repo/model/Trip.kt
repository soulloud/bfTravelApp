package com.beaconfire.travel.repo.model

data class Trip(
    val tripId: String,
    val owner: String,
    val destinations: List<Destination>,
    val collaborators: List<String>,
    val duration: String,
    val title: String,
    val description: String,
    val visibility: Visibility,
    val numPeople: Long,
) {
    sealed interface Visibility {
        val value: String

        data class Public(override val value: String = "public") : Visibility
        data class Private(override val value: String = "private") : Visibility
        data class Unknown(override val value: String = "") : Visibility

        fun toggle() = when (this) {
            is Public -> Private()
            is Private -> Public()
            else -> Unknown()
        }

        companion object {
            fun fromString(visibility: String?) = when (visibility) {
                "public" -> Public()
                "private" -> Private()
                else -> Unknown()
            }
        }
    }
}
