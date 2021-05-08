package com.example.projekt1.models

import java.io.Serializable

data class Location(
    val title: String,
    val location_type: String,
    val latt_long: String,
    val time: String,
    val sun_rise: String,
    val sun_set: String,
    val timezone_name: String,
    val consolidated_weather: List<ConsolidatedWeather>,
    val parent: LocationResponse
) : Serializable
