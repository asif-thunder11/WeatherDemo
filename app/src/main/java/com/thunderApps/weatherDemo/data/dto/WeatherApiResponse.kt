package com.thunderApps.weatherDemo.data.dto

import com.thunderApps.weatherDemo.data.entity.WeatherEntity


data class WeatherApiResponse (
    val coord: Coord,
    val weather: List<WeatherDescription>,
    val base: String,
    val main: WeatherMain,
    val visibility: Int,
    val wind: WeatherWind,
    val cod: Int,
    val dt: Long,
    val name: String,
    val id: Long,
    val timezone: Int,
)

fun WeatherApiResponse.toWeatherEntity(): WeatherEntity {
    return WeatherEntity(
        dt,
        coord.lat,
        coord.lon,
        weather,
        base,
        main.temp,
        main.feels_like,
        main.temp_min,
        main.temp_max,
        main.pressure,
        main.humidity,
        visibility,
        wind.speed,
        wind.deg,
        wind.gust,
        cod,
        name,
        id,
        timezone
    )
}
