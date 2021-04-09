package com.example.projekt1.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt1.models.Location
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.networking.RetrofitBuilder
import kotlinx.coroutines.launch
import java.io.IOException

class LocationViewModel : ViewModel() {

    val locationResponses = MutableLiveData<ArrayList<LocationResponse>>()
    val locations = MutableLiveData<ArrayList<Location>>()
    val isLoading = MutableLiveData<Boolean>()
    val specificLocation = MutableLiveData<Location>()

    fun getResponses(search: String, context: Context?) {
        viewModelScope.launch {
            var temp = mutableListOf<LocationResponse>()
            try {
                temp =
                    RetrofitBuilder.apiService.getLocations(search) as MutableList<LocationResponse>
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "You might wanna check your internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            locationResponses.value = temp as ArrayList<LocationResponse>
        }

    }

    fun getSpecificResponse(woeid: String, context: Context?) {
        viewModelScope.launch {
            var temp: Location? = null
            try {
                temp = RetrofitBuilder.apiService.getSpecificLocation(woeid)
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "You might wanna check your internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            specificLocation.value = temp!!

        }

    }

    /*fun getResponses(search: String, context: Context?){
        viewModelScope.launch {

            coroutineScope {
                async {
                    var temp = mutableListOf<LocationResponse>()
                    try {
                        temp = RetrofitBuilder.apiService.getLocations(search) as MutableList<LocationResponse>
                    }catch (e: IOException){
                        Toast.makeText(context,"You might wanna check your internet connection!", Toast.LENGTH_SHORT).show()
                    }

                    locationResponses.value = temp as ArrayList<LocationResponse>
                }.await()
                println(locationResponses.value?.size)
                locationResponses.value?.let { repeat(it.size){
                    async {
                        var temp: Location? = null
                        try {
                            temp = RetrofitBuilder.apiService.getSpecificLocation(locationResponses.value!!.get(it).woeid.toString())
                        }catch (e: IOException){
                            Toast.makeText(context,"You might wanna check your internet connection!", Toast.LENGTH_SHORT).show()
                        }

                        temp?.let { it1 -> locations.value?.add(it1) }
                    }.await()
                } }
                println("lokejšns" + locations.value)
            }

        }

    }*/

    /*fun getSpecificResponses(context: Context?){
        viewModelScope.launch {
            isLoading.value = true
            coroutineScope {
                locationResponses.value?.let {
                    repeat(it.size){
                        async {
                            var temp : Location? = null
                            try {
                                temp = RetrofitBuilder.apiService.getSpecificLocation(
                                    locationResponses.value!![it].woeid.toString()) as Location
                            }catch (e: IOException){
                                Toast.makeText(context,"You might wanna check your internet connection!", Toast.LENGTH_SHORT).show()
                            }

                            if (temp != null) {
                                println("Temp " + temp)
                                locations.value?.add(temp)
                            }
                        }.await()
                    }
                    isLoading.value = false
                }
            }
        }

    }*/


}