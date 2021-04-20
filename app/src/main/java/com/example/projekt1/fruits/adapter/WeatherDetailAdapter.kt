package com.example.projekt1.fruits.adapter

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

    /*private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvValue: TextView

    override fun getCount(): Int {
        println("SIZE " + names.size)
        return names.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.weather_element, null)
        }
        imageView = convertView!!.findViewById(R.id.imgWD)
        tvName = convertView.findViewById(R.id.tvWD)
        tvValue = convertView.findViewById(R.id.tvWDValue)

        imageView.load(R.drawable.ic_accuracy)
        tvName.text = names[position]
        tvValue.text = values[position]
        return convertView
    }*/


}