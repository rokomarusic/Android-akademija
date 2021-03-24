package com.example.projekt1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt1.add.AddFragment
import com.example.projekt1.databinding.ActivityMainBinding
import com.example.projekt1.fruits.FruitFragment2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addFragment = AddFragment()
        val fruitFragment = FruitFragment2()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, addFragment)
            commit()
        }

        bottomnv.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.miAddFruit) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayout, addFragment)
                    commit()
                }
            } else {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayout, fruitFragment)
                    commit()
                }
            }
            true
        }


    }

}