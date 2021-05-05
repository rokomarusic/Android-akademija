package com.example.projekt1.city.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.projekt1.R
import com.example.projekt1.city.CityActivity
import com.example.projekt1.databinding.ListItemBinding
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.viewmodel.LocationViewModel

class LocationAdapter(
    private val context: Context,
    private val values: ArrayList<LocationResponse>,
    private val model: LocationViewModel
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        println("tutututut")
        val item = values[position]
        holder.binding.tvRecentItemName.text = item.title
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
            } else {
                holder.binding.favImg.load(R.drawable.ic_star_0) { size(64) }
                if (model.locationResponsesDB.value?.contains(item) == true) {
                    model.locationResponsesDB.value?.remove(item)
                }
            }
        }


        holder.binding.root.setOnClickListener {
            item.recent = (model.recentDB.value?.size ?: 0) + 1
            model.updateLocationDB(item, holder.binding.root.context)
            val intent = Intent(context, CityActivity::class.java)
            intent.putExtra("EXTRA_LOCATION_RESPONSE", item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }
}