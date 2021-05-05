package com.example.projekt1.city

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.projekt1.R
import com.example.projekt1.R.string.*
import com.example.projekt1.city.adapter.NextSevenDaysAdapter
import com.example.projekt1.city.adapter.WeatherDetailAdapter
import com.example.projekt1.databinding.ActivityFruitBinding
import com.example.projekt1.models.ConsolidatedWeather
import com.example.projekt1.models.Location
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.models.WeatherDetail
import com.example.projekt1.viewmodel.LocationViewModel
import java.time.ZonedDateTime
import kotlin.math.roundToInt

class CityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitBinding
    private val model: LocationViewModel by viewModels()
    private lateinit var location: Location


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val locationResponse = intent.extras?.get("EXTRA_LOCATION_RESPONSE") as LocationResponse

        supportActionBar?.title = locationResponse.title

        println("lokejšn " + model.specificLocation.value)
        model.specificLocation.observe(this, Observer {
            setLocation(it)
        })
        model.getSpecificResponse(locationResponse.woeid.toString(), binding.root.context)

        /*model.getLocationDay(locationResponse.woeid.toString(), "2017/02/02", binding.root.context)

        model.locationDayList.observe(this, Observer {
            setLocationDay(it)
        })*/


        if (locationResponse.isFavourite) {
            binding.favImg.load(R.drawable.ic_star_1) { size(64) }
        } else {
            binding.favImg.load(R.drawable.ic_star_0) { size(64) }
        }

        binding.favImg.setOnClickListener {
            model.updateLocationDB(locationResponse, binding.root.context)
            locationResponse.isFavourite = !locationResponse.isFavourite
            if (locationResponse.isFavourite) {
                binding.favImg.load(R.drawable.ic_star_1) { size(64) }
                model.locationResponsesDB.value?.add(locationResponse)
            } else {
                binding.favImg.load(R.drawable.ic_star_0) { size(64) }
                if (model.locationResponsesDB.value?.contains(locationResponse) == true) {
                    model.locationResponsesDB.value?.remove(locationResponse)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLocation(data: Location) {
        println("lokacija " + data)
        location = Location(
            data.title,
            data.location_type,
            data.latt_long,
            data.time,
            data.sun_rise,
            data.sun_set,
            data.timezone_name,
            data.consolidated_weather
        )




        println("A")
        val realTime = ZonedDateTime.parse(location.time)
        println("B")

        binding.basicInfo.tvDate.text = resources.getString(
            dateFormatted,
            realTime.dayOfWeek.name.subSequence(0, 3),
            realTime.month.name,
            realTime.dayOfMonth.toString()
        )
        binding.basicInfo.tvTime.text = resources.getString(
            timeFormatted,
            realTime.hour.toString(),
            realTime.minute.toString(),
            realTime.zone.id
        )
        binding.basicInfo.tvWeather.text = location.consolidated_weather[0].weather_state_name
        binding.basicInfo.tvTemp.text = resources.getString(
            tempFormated,
            location.consolidated_weather[0].the_temp.roundToInt().toString()
        )

        when (location.consolidated_weather[0].weather_state_abbr) {
            "sn" -> binding.basicInfo.imgWeather.load(R.drawable.ic_sn) { size(64) }
            "sl" -> binding.basicInfo.imgWeather.load(R.drawable.ic_sl) { size(64) }
            "h" -> binding.basicInfo.imgWeather.load(R.drawable.ic_h) { size(64) }
            "t" -> binding.basicInfo.imgWeather.load(R.drawable.ic_t) { size(64) }
            "hr" -> binding.basicInfo.imgWeather.load(R.drawable.ic_hr) { size(64) }
            "lr" -> binding.basicInfo.imgWeather.load(R.drawable.ic_lr) { size(64) }
            "s" -> binding.basicInfo.imgWeather.load(R.drawable.ic_s) { size(64) }
            "hc" -> binding.basicInfo.imgWeather.load(R.drawable.ic_hc) { size(64) }
            "lc" -> binding.basicInfo.imgWeather.load(R.drawable.ic_lc) { size(64) }
            "c" -> binding.basicInfo.imgWeather.load(R.drawable.ic_c) { size(64) }
        }

        val details = mutableListOf<WeatherDetail>()
        details.add(
            WeatherDetail(
                getString(minmax), getString(
                    minmax_value,
                    location.consolidated_weather[0].min_temp.roundToInt().toString(),
                    location.consolidated_weather[0].max_temp.roundToInt().toString()
                ), R.drawable.ic_thermostat
            )
        )
        details.add(
            WeatherDetail(
                getString(wind), getString(
                    wind_value,
                    location.consolidated_weather[0].wind_speed.roundToInt().toString(),
                    location.consolidated_weather[0].wind_direction_compass
                ), R.drawable.ic_wind
            )
        )
        details.add(
            WeatherDetail(
                getString(humidity), getString(
                    humidity_value,
                    location.consolidated_weather[0].humidity.roundToInt().toString()
                ), R.drawable.ic_humidity
            )
        )
        details.add(
            WeatherDetail(
                getString(pressure), getString(
                    pressure_value,
                    location.consolidated_weather[0].air_pressure.roundToInt().toString()
                ), R.drawable.ic_pressure
            )
        )
        details.add(
            WeatherDetail(
                getString(visibility), getString(
                    visibility_value,
                    location.consolidated_weather[0].visibility.roundToInt().toString()
                ), R.drawable.ic_visibility
            )
        )
        details.add(
            WeatherDetail(
                getString(accuracy), getString(
                    accuracy_value,
                    location.consolidated_weather[0].predictability.roundToInt().toString()
                ), R.drawable.ic_accuracy
            )
        )


        val wdadapter = WeatherDetailAdapter(this, details)
        binding.gw.layoutManager = GridLayoutManager(this, 3)
        binding.gw.adapter = wdadapter

        val cwadapter = NextSevenDaysAdapter(this, location.consolidated_weather)
        binding.next7days.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.next7days.adapter = cwadapter

        /*runBlocking {
            model.getLocationDay(
                location.parent.woeid.toString(),
                "2017/02/02",
                binding.root.context
            )
        }

        model.locationDayList.observe(this, Observer {
            setLocationDay(it)
        })*/


    }

    private fun setLocationDay(it: ArrayList<ConsolidatedWeather>?) {
        val ldadapter = it?.let { it1 -> NextSevenDaysAdapter(this, it1) }
        binding.today.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.today.adapter = ldadapter
        println("ITT " + it)
    }
}