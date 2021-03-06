package com.example.projekt1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    var showing: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val showHide : Button = findViewById(R.id.btShowHide)
        val hw : TextView = findViewById(R.id.tvHelloWorld)

        showHide.setOnClickListener {
            if(showing){
                hw.visibility = View.INVISIBLE
                showHide.text = "Show"
            }else{
                hw.visibility = View.VISIBLE
                showHide.text = "Hide"
            }
            showing = !showing
        }
    }
}