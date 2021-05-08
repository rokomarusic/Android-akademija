package com.example.projekt1.city

import android.content.Intent
import android.net.Uri
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
import com.example.projekt1.city.adapter.TodayAdapter
import com.example.projekt1.city.adapter.WeatherDetailAdapter
import com.example.projekt1.databinding.ActivityFruitBinding
import com.example.projekt1.models.ConsolidatedWeather
import com.example.projekt1.models.Location
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.models.WeatherDetail
import com.example.projekt1.util.Util
import com.example.projekt1.viewmodel.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.time.ZonedDateTime
import kotlin.math.roundToInt

class CityActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityFruitBinding
    private val model: LocationViewModel by viewModels()
    private lateinit var location: Location
    private var latt: Double = 0.0
    private var long: Double = 0.0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)
        binding.map.onCreate(mapViewBundle)

        val locationResponse = intent.extras?.get("EXTRA_LOCATION_RESPONSE") as LocationResponse
        val isMetric = intent.extras?.get("IS_METRIC") as Boolean

        supportActionBar?.title = locationResponse.title

        println("lokejšn " + model.specificLocation.value)
        model.specificLocation.observe(this, Observer {
            setLocation(it, isMetric)
            binding.map.getMapAsync(this)
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
    private fun setLocation(data: Location, isMetric: Boolean) {
        println("lokacija " + data)
        location = Location(
            data.title,
            data.location_type,
            data.latt_long,
            data.time,
            data.sun_rise,
            data.sun_set,
            data.timezone_name,
            data.consolidated_weather,
            data.parent
        )




        println("AAAAA" + location.latt_long)
        val latt_long_temp = location.latt_long.split(",")
        latt = latt_long_temp[0].toDouble()
        long = latt_long_temp[1].toDouble()


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
        if (isMetric == true) {
            binding.basicInfo.tvTemp.text = resources.getString(
                tempFormated,
                location.consolidated_weather[0].the_temp.roundToInt().toString()
            )
        } else {
            val fahrtemp = Util.celToFahr(location.consolidated_weather[0].the_temp)
            binding.basicInfo.tvTemp.text = resources.getString(
                tempFormated,
                fahrtemp.roundToInt().toString()
            )
        }

        binding.basicInfo.imgWeather.load(Util.getWeatherImg(location.consolidated_weather[0].weather_state_abbr)) {
            size(
                64
            )
        }


        val details = mutableListOf<WeatherDetail>()
        println("is metric " + model.metric.value)
        details.add(
            WeatherDetail(
                getString(minmax), getString(
                    minmax_value,
                    if (isMetric == true)
                        location.consolidated_weather[0].min_temp.roundToInt().toString()
                    else Util.celToFahr(location.consolidated_weather[0].min_temp).roundToInt()
                        .toString(),
                    if (isMetric == true)
                        location.consolidated_weather[0].max_temp.roundToInt().toString()
                    else Util.celToFahr(location.consolidated_weather[0].max_temp).roundToInt()
                        .toString(),
                ), R.drawable.ic_thermostat
            )
        )

        if (isMetric) {
            details.add(
                WeatherDetail(
                    getString(wind), getString(
                        wind_value,
                        location.consolidated_weather[0].wind_speed.roundToInt().toString(),
                        location.consolidated_weather[0].wind_direction_compass
                    ), R.drawable.ic_wind
                )
            )
        } else {
            details.add(
                WeatherDetail(
                    getString(wind), getString(
                        wind_value_imperial,
                        Util.kmToMiles(location.consolidated_weather[0].wind_speed).roundToInt()
                            .toString(),
                        location.consolidated_weather[0].wind_direction_compass
                    ), R.drawable.ic_wind
                )
            )
        }
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
        if (isMetric) {
            details.add(
                WeatherDetail(
                    getString(visibility), getString(
                        visibility_value,
                        location.consolidated_weather[0].visibility.roundToInt().toString()
                    ), R.drawable.ic_visibility
                )
            )
        } else {
            details.add(
                WeatherDetail(
                    getString(visibility), getString(
                        visibility_value_imperial,
                        Util.kmToMiles(location.consolidated_weather[0].visibility).roundToInt()
                            .toString()
                    ), R.drawable.ic_visibility
                )
            )
        }

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

        val cwadapter = NextSevenDaysAdapter(this, location.consolidated_weather, isMetric)
        binding.next7days.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.next7days.adapter = cwadapter



        model.locationDayList.observe(
            this,
            {
                val todayWeather = mutableListOf<ConsolidatedWeather>()

                for (i in 0..23) {
                    model.locationDayList.value?.let { todayWeather.add(it.get(i)) }
                }
                val todayadapter = TodayAdapter(this, todayWeather, isMetric)
                binding.today.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.today.adapter = todayadapter
            }
        )

        binding.btnViewMore.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:" + location.latt_long + "?z=7f")
            println("gmmintenturi " + gmmIntentUri)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(packageManager)?.let {
                startActivity(mapIntent)
            }
        }


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

    private fun setLocationDay(it: ArrayList<ConsolidatedWeather>?, isMetric: Boolean) {
        val ldadapter = it?.let { it1 -> NextSevenDaysAdapter(this, it1, isMetric) }
        binding.today.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.today.adapter = ldadapter
        println("ITT " + it)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().also {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, it)
        }
        binding.map.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
    }

    override fun onMapReady(map: GoogleMap) {
        map.addMarker(MarkerOptions().position(LatLng(latt, long)).title("Marker"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latt, long), 7f))
        map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false
    }

    override fun onPause() {
        binding.map.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.map.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }
}