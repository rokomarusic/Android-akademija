package com.example.projekt1.fruits

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.load
import com.example.projekt1.R
import com.example.projekt1.R.string.*
import com.example.projekt1.databinding.ActivityFruitBinding
import com.example.projekt1.models.Location
import com.example.projekt1.models.LocationResponse
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


        /*model.getSpecificLocation(locationResponse.woeid.toString())
            .observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.root.visibility = View.VISIBLE
                            resource.data?.let { data -> setLocation(data) }
                        }
                        Status.ERROR -> {
                            binding.root.visibility = View.VISIBLE
                            Toast.makeText(
                                binding.root.context,
                                it.message + " " + it.data,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        Status.LOADING -> {
                            binding.root.visibility = View.GONE
                        }
                    }
                }
            })*/

        println("lokejšn " + model.specificLocation.value)
        model.specificLocation.observe(this, Observer {
            setLocation(it)
        })
        model.getSpecificResponse(locationResponse.woeid.toString(), binding.root.context)

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
            data.parent,
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

        binding.weatherDetails.tvMinMax.text = getString(minmax)
        binding.weatherDetails.tvWind.text = getString(wind)
        binding.weatherDetails.tvHumidity.text = getString(humidity)
        binding.weatherDetails.tvPressure.text = getString(pressure)
        binding.weatherDetails.tvVisibility.text = getString(visibility)
        binding.weatherDetails.tvAccuracy.text = getString(accuracy)

        binding.weatherDetails.tvTemp.text = getString(
            minmax_value,
            location.consolidated_weather[0].min_temp.roundToInt().toString(),
            location.consolidated_weather[0].max_temp.roundToInt().toString()
        )
        binding.weatherDetails.tvWindSpeed.text = getString(
            wind_value,
            location.consolidated_weather[0].wind_speed.roundToInt().toString(),
            location.consolidated_weather[0].wind_direction_compass
        )
        binding.weatherDetails.tvHumidityPer.text = getString(
            humidity_value,
            location.consolidated_weather[0].humidity.roundToInt().toString()
        )
        binding.weatherDetails.tvhPA.text = getString(
            pressure_value,
            location.consolidated_weather[0].air_pressure.roundToInt().toString()
        )
        binding.weatherDetails.tvVisibilityKm.text = getString(
            visibility_value,
            location.consolidated_weather[0].visibility.roundToInt().toString()
        )
        binding.weatherDetails.tvAccuracyPerc.text = getString(
            accuracy_value,
            location.consolidated_weather[0].predictability.roundToInt().toString()
        )


    }
}