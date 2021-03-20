package com.example.projekt1.fruits

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.R
import com.example.projekt1.databinding.FragmentItemBinding
import com.example.projekt1.models.Fruit

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyFruitRecyclerViewAdapter(
        private val values: List<Fruit>)
    : RecyclerView.Adapter<MyFruitRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemName.text = item.name
        holder.itemPrice.text =
            holder.itemView.resources.getString(R.string.dollar, item.price.toString())
        holder.itemQuantity.text =
            holder.itemView.resources.getString(R.string.pieces, item.quantity.toString())
        holder.itemColor.text = item.color
        holder.itemWeight.text =
            holder.itemView.resources.getString(R.string.kilo, item.weight.toString())
        holder.itemOrigin.text = item.countryOfOrigin
        holder.itemExotic.text =
            if (item.exotic) holder.itemView.resources.getString(R.string.exotic) else " "
        holder.itemSeasonal.text =
            if (item.seasonal) holder.itemView.resources.getString(R.string.seasonal) else " "
        holder.itemRipe.text =
            if (item.ripe) holder.itemView.resources.getString(R.string.ripe) else " "
        holder.itemType.text = item.type
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemName: TextView = binding.itemName
        val itemPrice: TextView = binding.itemPrice
        val itemQuantity: TextView = binding.itemQuantity
        val itemColor: TextView = binding.itemColor
        val itemWeight: TextView = binding.itemWeight
        val itemOrigin: TextView = binding.itemOrigin
        val itemExotic: TextView = binding.itemExotic
        val itemSeasonal: TextView = binding.itemSeasonal
        val itemRipe: TextView = binding.itemRipe
        val itemType: TextView = binding.itemType

        override fun toString(): String {
            return super.toString()
        }
    }
}