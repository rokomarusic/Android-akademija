package com.example.projekt1.favs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.clear
import coil.load
import com.example.projekt1.R
import com.example.projekt1.city.CityActivity
import com.example.projekt1.databinding.ListItemBinding
import com.example.projekt1.models.LocationResponse
import com.example.projekt1.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.list_item.view.*

class FavsAdapter(
    private val context: Context,
    val values: ArrayList<LocationResponse>,
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
        values.removeAt(from)
        /*if (to < from) {
            values.add(to, fromValue)
        } else {
            values.add(to - 1, fromValue)
        }*/
        values.add(to, fromValue)
    }


    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        println("onbind")
        val item = values[position]
        holder.binding.tvRecentItemName.text = item.title
        if (item.isFavourite) {
            holder.binding.favImg.load(R.drawable.ic_star_1) { size(64) }
        } else {
            holder.binding.favImg.load(R.drawable.ic_star_0) { size(64) }
        }
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