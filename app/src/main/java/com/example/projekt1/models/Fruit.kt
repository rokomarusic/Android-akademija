package com.example.projekt1.models

import java.io.Serializable

data class Fruit(
    val name: String,
    val image: String,
    val price: Double,
    val quantity: Int,
    val color: String,
    val weight: Double,
    val ripe: Boolean = true,
    val type: String,
    val countryOfOrigin: String,
    val exotic: Boolean,
    val seasonal: Boolean
) : Serializable