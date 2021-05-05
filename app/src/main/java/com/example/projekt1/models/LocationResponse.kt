package com.example.projekt1.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class LocationResponse(
    val title: String,
    val location_type: String,
    @PrimaryKey
    val woeid: Int,
    val latt_long: String,
    var isFavourite: Boolean = false,
    var recent: Int = -1
) : Serializable

