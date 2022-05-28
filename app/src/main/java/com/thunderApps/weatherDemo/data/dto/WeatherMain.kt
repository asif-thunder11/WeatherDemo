package com.thunderApps.weatherDemo.data.dto

import com.squareup.moshi.Json
import java.lang.reflect.Field

data class WeatherMain(
    val temp: Float,
    val feels_like: Float? = null,
    val temp_min: Float? = null,
    val temp_max: Float? = null,
    val pressure: Float? = null,
    val humidity: Float? = null
)
