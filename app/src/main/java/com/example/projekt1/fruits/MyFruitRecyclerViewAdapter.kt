package com.example.projekt1.fruits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.R
import com.example.projekt1.models.Fruit

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyFruitRecyclerViewAdapter(
        private val values: List<Fruit>)
    : RecyclerView.Adapter<MyFruitRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = holder.itemView.resources.getString(R.string.dollar, item.price.toString())
        holder.itemQuantity.text = holder.itemView.resources.getString(R.string.pieces, item.quantity.toString())
        holder.itemColor.text = item.color
        holder.itemWeight.text = holder.itemView.resources.getString(R.string.kilo, item.weight.toString())
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val itemQuantity: TextView = view.findViewById(R.id.itemQuantity)
        val itemColor: TextView = view.findViewById(R.id.itemColor)
        val itemWeight: TextView = view.findViewById(R.id.itemWeight)

        override fun toString(): String {
            return super.toString()
        }
    }
}