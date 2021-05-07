package com.example.projekt1.favs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.clear
import coil.load
import com.example.projekt1.R
import com.example.projekt1.city.CityActivity
import com.example.projekt1.databinding.ListItemBinding
import com.example.projekt1.models.Location
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.util.Util
import com.example.projekt1.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.list_item.view.*
import java.time.ZonedDateTime
import kotlin.math.roundToInt

class FavsAdapter(
    private val context: Context,
    val values: ArrayList<LocationResponse>,
    val locations: ArrayList<Location>,
    private val model: LocationViewModel,
    private val fragment: FavsFragment
) : RecyclerView.Adapter<FavsAdapter.LocationViewHolder>() {

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ListItemBinding.bind(view)
    }

    var reorderEnabled: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val lvh = LocationViewHolder(view)

        lvh.itemView.imgReorder.setOnTouchListener { view, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                fragment.startDragging(lvh)
            }
            return@setOnTouchListener true

        }
        return lvh
    }

    fun moveItem(from: Int, to: Int) {
        val fromValue = values[from]
        val fromLocation = locations[from]
        values.removeAt(from)
        locations.removeAt(from)
        /*if (to < from) {
            values.add(to, fromValue)
        } else {
            values.add(to - 1, fromValue)
        }*/
        values.add(to, fromValue)
        locations.add(to, fromLocation)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        println("onbind")
        println("aaaa " + values.map { it -> it.title })
        println("aaaa " + locations?.map { it -> it.title })
        val item = values[position]
        val location = locations.get(position)

        val realTime = ZonedDateTime.parse(location.time)

        holder.binding.tvRecentItemName.text = item.title
        holder.binding.tvTemp.text = context.resources.getString(
            R.string.tempFormated,
            location.consolidated_weather[0].the_temp.roundToInt().toString()
        )
        holder.binding.tvTime.text = context.resources.getString(
            R.string.shortTimeFormatted,
            realTime.hour.toString(),
            realTime.minute.toString()
        )
        holder.binding.tvTimeZone.text = realTime.zone.normalized().toString()
        holder.binding.weatherImg.load(Util.getWeatherImg(location.consolidated_weather[0].weather_state_abbr)) {
            size(
                64
            )
        }
        if (item.isFavourite) {
            holder.binding.favImg.load(R.drawable.ic_star_1) { size(64) }
        } else {
            holder.binding.favImg.load(R.drawable.ic_star_0) { size(64) }
        }
        holder.binding.favImg.setOnClickListener {
            item.isFavourite = !item.isFavourite
            if (item.isFavourite) {
                holder.binding.favImg.load(R.drawable.ic_star_1) { size(64) }
                model.locationResponsesDB.value?.add(item)
                model.favLocations.value?.add(location)
            } else {
                holder.binding.favImg.load(R.drawable.ic_star_0) { size(64) }
                if (model.locationResponsesDB.value?.contains(item) == true) {
                    println(
                        "item item " + item.title + " index " + model.locationResponsesDB?.value!!.indexOf(
                            item
                        )
                    )
                    val index = model.locationResponsesDB.value!!.indexOf(item)
                    model.locationResponsesDB.value?.removeAt(index)
                    model.favLocations.value?.removeAt(index)
                }
            }
            model.updateLocationDB(item, holder.binding.root.context)
        }

        holder.binding.root.setOnClickListener {
            item.recent = (model.recentDB.value?.size ?: 0) + 1
            model.updateLocationDB(item, holder.binding.root.context)
            val intent = Intent(context, CityActivity::class.java)
            intent.putExtra("EXTRA_LOCATION_RESPONSE", item)
            context.startActivity(intent)
        }

        if (reorderEnabled) {
            holder.binding.imgReorder.load(R.drawable.ic_reorder) { size(64) }
        } else {
            holder.binding.imgReorder.clear()
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

}