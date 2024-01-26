package com.beaconfire.travel.repo

import com.beaconfire.travel.repo.model.User

class UserRepository {

    fun login(username: String, password: String) = User.DUMMY_VALID_USER

    fun register(email: String, username: String, password: String) = User.DUMMY_VALID_USER

}
