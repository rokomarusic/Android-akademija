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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class NextSevenDaysAdapter(
    private val context: Context,
    private val cw: List<ConsolidatedWeather>
) : RecyclerView.Adapter<NextSevenDaysAdapter.SimpleWeatherItemViewHolder>() {

    class SimpleWeatherItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = SimpleWeatherItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleWeatherItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.simple_weather_item, parent, false)
        return NextSevenDaysAdapter.SimpleWeatherItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SimpleWeatherItemViewHolder, position: Int) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var date = LocalDate.parse(cw[position].applicable_date, formatter)
        holder.binding.tvSWItext.text = date.dayOfWeek.toString().substring(0, 3)
        holder.binding.tvTemp.text = holder.binding.root.context.getString(
            R.string.tempFormated,
            cw[position].the_temp.roundToInt().toString()
        )
        when (cw[position].weather_state_abbr) {
            "sn" -> holder.binding.imgSWI.load(R.drawable.ic_sn) { size(64) }
            "sl" -> holder.binding.imgSWI.load(R.drawable.ic_sl) { size(64) }
            "h" -> holder.binding.imgSWI.load(R.drawable.ic_h) { size(64) }
            "t" -> holder.binding.imgSWI.load(R.drawable.ic_t) { size(64) }
            "hr" -> holder.binding.imgSWI.load(R.drawable.ic_hr) { size(64) }
            "lr" -> holder.binding.imgSWI.load(R.drawable.ic_lr) { size(64) }
            "s" -> holder.binding.imgSWI.load(R.drawable.ic_s) { size(64) }
            "hc" -> holder.binding.imgSWI.load(R.drawable.ic_hc) { size(64) }
            "lc" -> holder.binding.imgSWI.load(R.drawable.ic_lc) { size(64) }
            "c" -> holder.binding.imgSWI.load(R.drawable.ic_c) { size(64) }
        }
    }

    override fun getItemCount(): Int {
        return cw.size
    }


}