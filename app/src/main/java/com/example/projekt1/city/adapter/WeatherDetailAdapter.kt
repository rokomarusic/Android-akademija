package com.example.projekt1.city.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.projekt1.R
import com.example.projekt1.databinding.WeatherElementBinding
import com.example.projekt1.models.WeatherDetail

class WeatherDetailAdapter(
    private val context: Context,
    private val wd: MutableList<WeatherDetail>
) : RecyclerView.Adapter<WeatherDetailAdapter.WeatherElementViewHolder>() {

    class WeatherElementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = WeatherElementBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherElementViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.weather_element, parent, false)
        return WeatherElementViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherElementViewHolder, position: Int) {
        holder.binding.imgWD.load(wd[position].imgsrc)
        holder.binding.tvWD.text = wd[position].name
        holder.binding.tvWDValue.text = wd[position].value
    }

    override fun getItemCount(): Int {
        return wd.size
    }


}