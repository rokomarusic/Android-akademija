package com.example.projekt1.networking

import com.example.projekt1.models.Location
import com.example.projekt1.models.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("/api/location/search/?query=(query)")
    suspend fun getLocations(@Query("query") name: String): List<LocationResponse>

    @GET("/api/location/{woeid}/")
    suspend fun getSpecificLocation(@Path("woeid") woeid: String): Location
}