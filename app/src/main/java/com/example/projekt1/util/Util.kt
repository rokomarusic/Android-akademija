package com.example.projekt1.util

import com.example.projekt1.R

object Util {

    fun getWeatherImg(abbr: String): Int {
        when (abbr) {
            "sn" -> return R.drawable.ic_sn
            "sl" -> return R.drawable.ic_sl
            "h" -> return R.drawable.ic_h
            "t" -> return R.drawable.ic_t
            "hr" -> return R.drawable.ic_hr
            "lr" -> return R.drawable.ic_lr
            "s" -> return R.drawable.ic_s
            "hc" -> return R.drawable.ic_hc
            "lc" -> return R.drawable.ic_lc
            "c" -> return R.drawable.ic_c
        }
        return Int.MAX_VALUE
    }

    fun celToFahr(temp: Float): Float {
        return (temp * 1.8f) + 32f
    }

    fun kmToMiles(km: Float): Float {
        return km * 0.621371192f
    }

    fun getWeather(abbr: String): Int {
        when (abbr) {
            "sn" -> return R.string.snow
            "sl" -> return R.string.sleet
            "h" -> return R.string.hail
            "t" -> return R.string.thunderstorm
            "hr" -> return R.string.heavy_rain
            "lr" -> return R.string.light_rain
            "s" -> return R.string.showers
            "hc" -> return R.string.heavy_cloud
            "lc" -> return R.string.light_cloud
            "c" -> return R.string.clear_weather
        }
        return R.string.clear_weather
    }

    fun getDay(number: Int): Int {
        when (number) {
            1 -> return R.string.mon
            2 -> return R.string.tue
            3 -> return R.string.wen
            4 -> return R.string.thu
            5 -> return R.string.fri
            6 -> return R.string.sat
            7 -> return R.string.sun
        }
        return R.string.mon
    }

    fun getMonth(number: Int): Int {
        when (number) {
            1 -> return R.string.january
            2 -> return R.string.february
            3 -> return R.string.march
            4 -> return R.string.april
            5 -> return R.string.may
            6 -> return R.string.june
            7 -> return R.string.july
            8 -> return R.string.august
            9 -> return R.string.september
            10 -> return R.string.october
            11 -> return R.string.november
            12 -> return R.string.december
        }
        return R.string.mon
    }
}