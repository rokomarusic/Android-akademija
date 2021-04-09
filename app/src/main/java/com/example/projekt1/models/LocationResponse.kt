package com.example.projekt1.models

import java.io.Serializable

data class LocationResponse(
    val title: String,
    val location_type: String,
    val woeid: Int,
    val latt_long: String
) : Serializable

