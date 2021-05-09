package com.example.projekt1.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.projekt1.MainActivity
import com.example.projekt1.R
import com.example.projekt1.widget.constants.CITY_DATE_TEXT
import com.example.projekt1.widget.constants.CITY_NAME_TEXT
import com.example.projekt1.widget.constants.CITY_TEMP_TEXT
import com.example.projekt1.widget.constants.CITY_WEATHER_ABBR
import com.example.projekt1.widget.constants.SHARED_PRES

class WidgetProvider : AppWidgetProvider() {


    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

        if (appWidgetIds != null) {
            for (id in appWidgetIds) {
                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

                val prefs = context?.getSharedPreferences(SHARED_PRES, Context.MODE_PRIVATE)

                val cityName = prefs?.getString(CITY_NAME_TEXT + id, "City name")
                val cityTemp = prefs?.getString(CITY_TEMP_TEXT + id, "City temp")
                val cityDate = prefs?.getString(CITY_DATE_TEXT + id, "City date")

                val imgId = prefs?.getInt(CITY_WEATHER_ABBR + id, R.drawable.ic_logo_android)

                val views = RemoteViews(context?.packageName, R.layout.widget_layout)
                views.setCharSequence(R.id.tvCityNameWidget, "setText", cityName)
                views.setCharSequence(R.id.tvTempWidget, "setText", cityTemp)
                views.setCharSequence(R.id.tvDateWidget, "setText", cityDate)

                views.setOnClickPendingIntent(R.id.widget, pendingIntent)

                if (imgId != null) {
                    views.setImageViewResource(R.id.imgWeatherWidget, imgId)
                }

                if (appWidgetManager != null) {
                    appWidgetManager.updateAppWidget(id, views)
                }
            }
        }
    }
}