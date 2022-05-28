package com.thunderApps.weatherDemo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thunderApps.weatherDemo.WeatherApp
import com.thunderApps.weatherDemo.data.dto.*

/** Entity class for saving Weather data in DB*/
@Entity
data class WeatherEntity(
    @PrimaryKey var dt: Long,
    var lat: Float,
    var lon: Float,
    var weatherDescriptions: List<WeatherDescription>,
    var base: String,
    var temp: Float,
    var feels_like: Float?,
    var temp_min: Float?,
    var temp_max: Float?,
    var pressure: Float?,
    var humidity: Float?,
    var visibility: Int,
    var windSpeed: Float?,
    var windDeg: Float?,
    var windDust: Float?,
    var cod: Int,
    var name: String,
    var id: Long,
    var timezone: Int
)

/**Extension class to map WeatherEntity to WeatherApiResponse*/
fun WeatherEntity.toWeatherApiResponse(): WeatherApiResponse {
    return WeatherApiResponse(
        Coord(lat, lon),
        weatherDescriptions,
        base,
        WeatherMain(temp, feels_like, temp_min, temp_max, pressure, humidity),
        visibility,
        WeatherWind(windSpeed, windDeg, windDust),
        cod,
        dt,
        name,
        id,
        timezone
    )
}

/** Converter class for converting list of [WeatherDescription] to save in DB*/
class WeatherDescriptionConverter() {
    @TypeConverter
    fun weatherToString(weatherDescs: List<WeatherDescription>): String {
        return Gson().toJson(weatherDescs, object : TypeToken<List<WeatherDescription>>() {}.type)
    }

    @TypeConverter
    fun stringToWeather(weather: String): List<WeatherDescription> {
        return Gson().fromJson(weather, object : TypeToken<List<WeatherDescription>> () {}.type)
    }
}