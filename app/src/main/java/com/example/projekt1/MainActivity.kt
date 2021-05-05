package com.example.projekt1

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt1.city.SearchFragment
import com.example.projekt1.databinding.ActivityMainBinding
import com.example.projekt1.favs.FavsFragment
import com.example.projekt1.locale.MyContextWrapper
import com.example.projekt1.locale.MyPreference
import com.example.projekt1.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var preference: MyPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val addFragment = FavsFragment()
        val fruitFragment = SearchFragment()
        val settingsFragment = SettingsFragment()

        /*val snackbar =
            Snackbar.make(
                binding.root,
                getString(R.string.language_set_to) + " " + (if (preference.getLang()
                        .equals("hr")
                ) getString(R.string.cro) else getString(R.string.eng)),
                Snackbar.LENGTH_LONG
            ).setAction(getString(R.string.undo), View.OnClickListener {
                preference.setLang(if (preference.getLang().equals("en")) "hr" else "en")
                recreate()
            })
        snackbar.show()*/



        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fruitFragment)
            commit()
        }

        bottomnv.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.miAddFruit) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayout, addFragment)
                    commit()
                }
            } else if (it.itemId == R.id.miViewBasket) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayout, fruitFragment)
                    commit()
                }
            } else {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayout, settingsFragment)
                    commit()
                }
            }
            true
        }


    }

    override fun attachBaseContext(newBase: Context?) {
        preference = MyPreference(newBase!!)
        val lang = preference.getLang()
        super.attachBaseContext(MyContextWrapper.wrap(newBase, lang!!))
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}