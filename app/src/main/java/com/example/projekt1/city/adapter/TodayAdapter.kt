package com.example.projekt1.city.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.projekt1.R
import com.example.projekt1.databinding.SimpleWeatherItemBinding
import com.example.projekt1.models.ConsolidatedWeather
import com.example.projekt1.util.Util
import kotlin.math.roundToInt

class TodayAdapter(
    private val context: Context,
    private val cw: List<ConsolidatedWeather>,
    private val isMetric: Boolean,
) : RecyclerView.Adapter<TodayAdapter.SimpleWeatherItemViewHolder>() {

    private val hours = arrayOf(
        "00:00",
        "01:00",
        "02:00",
        "03:00",
        "04:00",
        "05:00",
        "06:00",
        "07:00",
        "08:00",
        "09:00",
        "10:00",
        "11:00",
        "12:00",
        "13:00",
        "14:00",
        "15:00",
        "16:00",
        "17:00",
        "18:00",
        "19:00",
        "20:00",
        "21:00",
        "22:00",
        "23:00"
    )

    class SimpleWeatherItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = SimpleWeatherItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleWeatherItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.simple_weather_item, parent, false)
        return TodayAdapter.SimpleWeatherItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SimpleWeatherItemViewHolder, position: Int) {
        holder.binding.tvSWItext.text = hours[position]
        holder.binding.tvTemp.text = holder.binding.root.context.getString(
            R.string.tempFormated,
            if (isMetric)
                cw[position].the_temp.roundToInt().toString()
            else
                Util.celToFahr(cw[position].the_temp).roundToInt().toString()
        )

        holder.binding.imgSWI.load(Util.getWeatherImg(cw[position].weather_state_abbr)) { size(64) }
    }

    override fun getItemCount(): Int {
        return cw.size
    }


}