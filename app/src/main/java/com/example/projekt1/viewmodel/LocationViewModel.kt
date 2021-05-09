package com.example.projekt1.viewmodel

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt1.db.DatabaseBuilder
import com.example.projekt1.models.ConsolidatedWeather
import com.example.projekt1.models.Location
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.networking.RetrofitBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocationViewModel : ViewModel() {

    val locationResponses = MutableLiveData<ArrayList<LocationResponse>>()
    val locationDayList = MutableLiveData<ArrayList<ConsolidatedWeather>>()
    val locationResponsesDB = MutableLiveData<ArrayList<LocationResponse>>()
    val recentDB = MutableLiveData<ArrayList<LocationResponse>>()
    val locations = MutableLiveData<ArrayList<Location>>()
    val favLocations = MutableLiveData<ArrayList<Location>>()
    val recentLocations = MutableLiveData<ArrayList<Location>>()
    val specificLocation = MutableLiveData<Location>()
    val hints = MutableLiveData<List<LocationResponse>>()
    val metric = MutableLiveData<Boolean>()

    init {
        metric.value = true
    }


    fun getHints(search: String, context: Context?) {
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

            hints.value = temp as ArrayList<LocationResponse>
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSpecificResponse(woeid: String, context: Context?) {
        viewModelScope.launch {
            var temp: Location? = null
            var hourByHour = mutableListOf<ConsolidatedWeather>()
            try {
                val waitingLocation =
                    async { RetrofitBuilder.apiService.getSpecificLocation(woeid) }
                temp = waitingLocation.await()
                val current = LocalDateTime.now()
                println("current " + current)
                val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                val formatted = current.format(formatter)
                val waitingHourbyHour =
                    async { RetrofitBuilder.apiService.getLocationDay(woeid, formatted) }
                hourByHour = waitingHourbyHour.await() as MutableList<ConsolidatedWeather>
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "You might wanna check your internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            specificLocation.value = temp!!
            locationDayList.value = hourByHour as ArrayList<ConsolidatedWeather>
        }
    }

    fun getLocations(search: String, context: Context?) {
        viewModelScope.launch {
            var temp: List<LocationResponse>? = null
            var locationsTemp: List<Location>? = null
            try {
                val waitingResponses = async { RetrofitBuilder.apiService.getLocations(search) }
                temp = waitingResponses.await()
                val waitingLocations = temp.map { locationResponse ->
                    async {
                        RetrofitBuilder.apiService.getSpecificLocation(locationResponse.woeid.toString())
                    }
                }
                locationsTemp = waitingLocations.awaitAll()
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "You might wanna check your internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            locationResponses.value = temp as ArrayList<LocationResponse>
            if (!locationsTemp.isNullOrEmpty()) {
                locations.value = (locationsTemp as ArrayList<Location>?)!!
            }
        }

    }

    fun insertLocationDB(locationResponse: LocationResponse, context: Context?) {
        viewModelScope.launch {
            if (context != null) {
                DatabaseBuilder.getInstance(context).userDao().insertLocation(locationResponse)
            }
        }
    }

    fun updateLocationDB(locationResponse: LocationResponse, context: Context?) {
        viewModelScope.launch {
            if (context != null) {
                DatabaseBuilder.getInstance(context).userDao().updateLocation(locationResponse)
            }
        }
    }

    fun deleteAllDB(context: Context?) {
        viewModelScope.launch {
            if (context != null) {
                DatabaseBuilder.getInstance(context).userDao().deleteAll()
            }
        }
    }

    fun clearFavouritesDB(context: Context?) {
        viewModelScope.launch {
            if (context != null) {
                DatabaseBuilder.getInstance(context).userDao().clearFavourites()
                locationResponsesDB.value?.clear()
                favLocations.value?.clear()
            }
        }
    }

    fun clearRecentDB(context: Context?) {
        viewModelScope.launch {
            if (context != null) {
                DatabaseBuilder.getInstance(context).userDao().clearRecent()
                recentDB.value?.clear()
                recentLocations.value?.clear()
            }
        }
    }


    fun selectFavouritesDB(context: Context?) {
        viewModelScope.launch {
            var temp = mutableListOf<LocationResponse>()
            var locations = mutableListOf<Location>()
            if (context != null) {
                val waitBase = async {
                    DatabaseBuilder.getInstance(context).userDao()
                        .getFavourites() as MutableList<LocationResponse>
                }
                temp = waitBase.await()
                val waitingLocations = temp.map { locationResponse ->
                    async {
                        RetrofitBuilder.apiService.getSpecificLocation(locationResponse.woeid.toString())
                    }
                }
                if (!waitingLocations.isNullOrEmpty()) {
                    locations = waitingLocations.awaitAll() as MutableList<Location>
                }
            }
            locationResponsesDB.value = temp as ArrayList<LocationResponse>
            favLocations.value = locations as ArrayList<Location>
        }
    }

    fun selectRecentDB(context: Context?) {
        viewModelScope.launch {
            var temp = mutableListOf<LocationResponse>()
            var locations = mutableListOf<Location>()
            if (context != null) {
                val waitBase = async {
                    DatabaseBuilder.getInstance(context).userDao()
                        .getRecent() as MutableList<LocationResponse>
                }
                temp = waitBase.await()
                val waitingLocations = temp.map { locationResponse ->
                    async {
                        RetrofitBuilder.apiService.getSpecificLocation(locationResponse.woeid.toString())
                    }
                }
                if (!waitingLocations.isNullOrEmpty()) {
                    locations = waitingLocations.awaitAll() as MutableList<Location>
                }
            }
            recentDB.value = temp as ArrayList<LocationResponse>
            recentLocations.value = locations as ArrayList<Location>

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