package com.example.projekt1.fruits.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.R
import com.example.projekt1.databinding.RecentItemBinding
import com.example.projekt1.fruits.CityActivity
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.viewmodel.LocationViewModel

class LocationAdapter(
    private val context: Context,
    private val values: ArrayList<LocationResponse>,
    private val model: LocationViewModel
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecentItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_item, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = values[position]
        holder.binding.tvRecentItemName.text = item.title
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, CityActivity::class.java)
            intent.putExtra("EXTRA_LOCATION_RESPONSE", item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }
}