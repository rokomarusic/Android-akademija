package com.example.projekt1.fruits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.projekt1.R
import com.example.projekt1.databinding.ActivityFruitBinding
import com.example.projekt1.models.Fruit

class FruitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val fruit = intent.extras?.get("extra_fruit") as Fruit

        supportActionBar?.setTitle(fruit.name)


        binding.fruitImg.load(fruit.image)

        binding.itemName.text = fruit.name
        binding.itemPrice.text = resources.getString(R.string.dollar, fruit.price.toString())
        binding.itemQuantity.text = resources.getString(R.string.pieces, fruit.quantity.toString())
        binding.itemColor.text = fruit.color
        binding.itemWeight.text = resources.getString(R.string.kilo, fruit.weight.toString())
        binding.itemOrigin.text = fruit.countryOfOrigin
        binding.itemType.text = fruit.type
        binding.itemExotic.text = if (fruit.exotic) resources.getString(R.string.exotic) else ""
        binding.itemRipe.text = if (fruit.ripe) resources.getString(R.string.ripe) else ""
        binding.itemSeasonal.text =
            if (fruit.seasonal) resources.getString(R.string.seasonal) else ""


    }
}