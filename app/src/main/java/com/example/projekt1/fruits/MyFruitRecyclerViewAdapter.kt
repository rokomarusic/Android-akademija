package com.example.projekt1.fruits

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projekt1.R
import com.example.projekt1.databinding.FragmentItemBinding
import com.example.projekt1.models.Fruit
import java.io.Serializable

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
        holder.itemImage.load(item.image) {
            transformations(CircleCropTransformation())
        }

        holder.itemImage.setOnClickListener {
            val intent = Intent(holder.itemImage.context, FruitActivity::class.java).apply {
                putExtra("extra_fruit", item as Serializable)
            }
            holder.itemImage.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemName: TextView = binding.itemName
        val itemPrice: TextView = binding.itemPrice
        val itemImage: ImageView = binding.fruitImg


        override fun toString(): String {
            return super.toString()
        }
    }
}