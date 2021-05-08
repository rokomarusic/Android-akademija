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
}