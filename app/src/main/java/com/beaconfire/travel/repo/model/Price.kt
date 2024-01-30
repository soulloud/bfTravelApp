package com.beaconfire.travel.repo.model

data class Price(
    val value: Double,
    val currency: String
) {
    companion object {
        val InvalidPrice = Price(-1.0, "INVALID")
    }
}
