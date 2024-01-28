package com.beaconfire.travel.repo.database.region

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "state_id")
    val stateId: Long,

    @ColumnInfo(name = "city")
    val city: String,
)
