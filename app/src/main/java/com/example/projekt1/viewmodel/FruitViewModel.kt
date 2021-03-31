package com.example.projekt1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projekt1.models.Fruit

class FruitViewModel() : ViewModel() {

    val fruits = MutableLiveData<ArrayList<Fruit>>()

    init {
        fruits.value = arrayListOf()
    }

}