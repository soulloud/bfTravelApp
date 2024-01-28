package com.beaconfire.travel.repo.model

import com.beaconfire.travel.repo.database.region.StateEntity

data class State(
    val id: Long? = null,
    val state: String
) {

    fun toStateEntity() = StateEntity(state = state)

    companion object {
        val INVALID_STATE = State(state = "")

        fun fromString(state: String) = State(state = state)

        fun fromStateEntity(stateEntity: StateEntity) = State(stateEntity.id, stateEntity.state)
    }
}
