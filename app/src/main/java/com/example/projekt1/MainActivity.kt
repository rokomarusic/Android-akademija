package com.example.projekt1

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt1.add.AddFragment
import com.example.projekt1.fruits.FruitFragment2

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addFragment = AddFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayout, addFragment)
                commit()
            }
        }


    }

    override fun onStart() {
        super.onStart()

        val addFragment = AddFragment()
        val fruitFragment = FruitFragment2()
        val btnAdd: Button = findViewById(R.id.btnAdd)
        val btnFruits: Button = findViewById(R.id.btnFruits)

        btnAdd.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayout, addFragment)
                commit()
            }
        }

        btnFruits.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayout, fruitFragment)
                commit()
            }
        }

    }
}