package com.example.projekt1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projekt1.models.Fruit

class FruitViewModel : ViewModel() {

    val fruits = MutableLiveData<List<Fruit>>()

    fun initFruits(): MutableLiveData<List<Fruit>> {
        if (!fruits.value.isNullOrEmpty()) {
            return fruits
        }
        fruits.value = mutableListOf()
        return fruits
    }

    fun addFruit(name: String, price: Double, quantity: Int, color: String, weight: Double) {
        val fruit = Fruit(name, price, quantity, color, weight)
        val temp = mutableListOf<Fruit>()
        fruits.value?.let { temp.addAll(it) }
        temp.add(fruit)
        fruits.value = temp
    }


}