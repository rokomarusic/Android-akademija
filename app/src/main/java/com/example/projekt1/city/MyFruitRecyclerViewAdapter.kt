package com.example.projekt1.city

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.R
import com.example.projekt1.databinding.FragmentItemBinding
import com.example.projekt1.models.LocationResponse

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyFruitRecyclerViewAdapter(
    private val context: Context,
    private val values: List<LocationResponse>
) : RecyclerView.Adapter<MyFruitRecyclerViewAdapter.LocationViewHolder>() {

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = FragmentItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_item, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = values[position]
        holder.binding.itemName.text = item.title
        holder.binding.itemName.setOnClickListener {
            context.startActivity(Intent(context, CityActivity::class.java))
        }

    }

    override fun getItemCount(): Int = values.size

}