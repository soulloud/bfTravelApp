package com.beaconfire.travel.repo.model

data class User(
    val username: String = "",
    val password: String = "",
) {
    companion object {
        val INVALID_USER = User()
        val DUMMY_VALID_USER = User(username = "valid", password = "")
    }
}
