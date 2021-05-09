package com.example.projekt1.city.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.projekt1.R
import com.example.projekt1.city.CityActivity
import com.example.projekt1.databinding.ListItemBinding
import com.example.projekt1.models.Location
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.util.Util
import com.example.projekt1.viewmodel.LocationViewModel
import java.time.ZonedDateTime
import kotlin.math.roundToInt

class LocationAdapter(
    private val context: Context,
    private val values: ArrayList<LocationResponse>,
    private val locations: ArrayList<Location>?,
    private val model: LocationViewModel,
    private val latt: Double,
    private val long: Double
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return LocationViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        println("tutututut")

        val item = values[position]
        val location = locations?.get(position)

        val realTime = ZonedDateTime.parse(location?.time)

        holder.binding.tvRecentItemName.text = item.title
        if (location != null) {
            holder.binding.tvTemp.text = context.resources.getString(
                R.string.tempFormated,
                if (model.metric.value == true)
                    location.consolidated_weather[0].the_temp.roundToInt().toString()
                else
                    Util.celToFahr(location.consolidated_weather[0].the_temp).roundToInt()
                        .toString()
            )
            if (latt != 0.0 || long != 0.0) {
                holder.binding.tvTime.text = item.latt_long
                holder.binding.tvTimeZone.text = context.resources.getString(
                    if (model.metric.value == true)
                        R.string.distance
                    else
                        R.string.distance_imperial,
                    calcDist(latt, long, position).roundToInt().toString()
                )

            } else {

                holder.binding.tvTime.text = context.resources.getString(
                    R.string.shortTimeFormatted,
                    realTime.hour.toString(),
                    realTime.minute.toString()
                )
                holder.binding.tvTimeZone.text = realTime.zone.normalized().toString()

            }
            holder.binding.weatherImg.load(Util.getWeatherImg(location.consolidated_weather[0].weather_state_abbr)) {
                size(
                    64
                )
            }
        }
        var favourite: Boolean = false
        if (model.locationResponsesDB.value != null) {
            for (i in model.locationResponsesDB.value!!) {
                if (i.woeid == item.woeid) {
                    favourite = i.isFavourite
                }
            }
        }
        if (favourite) {
            holder.binding.favImg.load(R.drawable.ic_star_1) { size(64) }
        } else {
            holder.binding.favImg.load(R.drawable.ic_star_0) { size(64) }
        }

        model.insertLocationDB(item, holder.binding.root.context)

        holder.binding.favImg.setOnClickListener {
            model.updateLocationDB(item, holder.binding.root.context)
            item.isFavourite = !item.isFavourite
            if (item.isFavourite) {
                holder.binding.favImg.load(R.drawable.ic_star_1) { size(64) }
                model.locationResponsesDB.value?.add(item)
                if (location != null) {
                    model.favLocations.value?.add(location)
                }
            } else {
                holder.binding.favImg.load(R.drawable.ic_star_0) { size(64) }
                if (model.locationResponsesDB.value?.contains(item) == true) {
                    model.locationResponsesDB.value?.remove(item)
                    model.favLocations.value?.remove(location)
                }
            }
        }


        holder.binding.root.setOnClickListener {
            item.recent = (model.recentDB.value?.size ?: 0) + 1
            model.updateLocationDB(item, holder.binding.root.context)
            val intent = Intent(context, CityActivity::class.java)
            intent.putExtra("EXTRA_LOCATION_RESPONSE", item)
            intent.putExtra("IS_METRIC", model.metric.value)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    fun calcDist(latt: Double, long: Double, pos: Int): Float {
        val x = android.location.Location("x")
        x.latitude = latt
        x.longitude = long
        val y = android.location.Location("y")
        val temp = values[pos].latt_long.split(",")
        y.latitude = temp[0].toDouble()
        y.longitude = temp[1].toDouble()
        return x.distanceTo(y) / 1000
    }
}