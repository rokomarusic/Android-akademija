package com.example.projekt1.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RemoteViews
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt1.MainActivity
import com.example.projekt1.R
import com.example.projekt1.util.Util
import com.example.projekt1.viewmodel.LocationViewModel
import com.example.projekt1.widget.constants.CITY_DATE_TEXT
import com.example.projekt1.widget.constants.CITY_NAME_TEXT
import com.example.projekt1.widget.constants.CITY_TEMP_TEXT
import com.example.projekt1.widget.constants.CITY_WEATHER_ABBR
import com.example.projekt1.widget.constants.SHARED_PRES
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class WidgetConfig : AppCompatActivity() {


    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    lateinit var buttonWidget: Button
    lateinit var metricButton: RadioButton
    private val model: LocationViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)


        val configIntent = intent;
        val extras = configIntent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        buttonWidget = findViewById(R.id.btnSetUnit)
        metricButton = findViewById(R.id.radioMetricWidget)

        model.getSpecificResponse("851128", this)

        model.specificLocation.observe(this, {
            println("u zagrebu je " + it.title + " " + it.consolidated_weather[0].the_temp)
            buttonWidget.isClickable = true
        })

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun confirmConfig(view: View) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val cityName = model.specificLocation.value?.title
        val cityTemp = resources.getString(R.string.tempFormated, if (metricButton.isChecked)
            model.specificLocation.value?.consolidated_weather?.get(0)?.the_temp?.roundToInt()
                .toString()
        else
            model.specificLocation.value?.consolidated_weather?.get(0)?.the_temp?.let {
                Util.celToFahr(
                    it
                ).roundToInt().toString()
            })
        val cityDate = model.specificLocation.value?.consolidated_weather?.get(0)?.applicable_date

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formatted = current.format(formatter)

        val views = RemoteViews(this.packageName, R.layout.widget_layout)
        views.setOnClickPendingIntent(R.id.tvCityNameWidget, pendingIntent)
        views.setCharSequence(R.id.tvCityNameWidget, "setText", cityName)
        views.setOnClickPendingIntent(R.id.tvTempWidget, pendingIntent)
        views.setCharSequence(R.id.tvTempWidget, "setText", cityTemp.toString())
        views.setOnClickPendingIntent(R.id.tvDateWidget, pendingIntent)
        views.setCharSequence(R.id.tvDateWidget, "setText", formatted)
        views.setOnClickPendingIntent(R.id.imgWeatherWidget, pendingIntent)
        model.specificLocation.value?.consolidated_weather?.get(0)?.let {
            views.setImageViewResource(R.id.imgWeatherWidget,
                it.let { Util.getWeatherImg(it.weather_state_abbr) }
            )
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)

        val prefs = getSharedPreferences(SHARED_PRES, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(CITY_NAME_TEXT + appWidgetId, cityName)
        editor.putString(CITY_TEMP_TEXT + appWidgetId, cityTemp.toString())
        editor.putString(CITY_DATE_TEXT + appWidgetId, formatted)
        model.specificLocation.value?.consolidated_weather?.get(0)?.let {
            editor.putInt(CITY_WEATHER_ABBR + appWidgetId,
                it.let { Util.getWeatherImg(it.weather_state_abbr) })
        }
        editor.apply()

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}

object constants {
    val SHARED_PRES: String = "prefs"
    val CITY_NAME_TEXT = "cityNameText"
    val CITY_TEMP_TEXT = "cityTempText"
    val CITY_DATE_TEXT = "cityDateText"
    val CITY_WEATHER_ABBR = "cityWeatherAbbr"
}