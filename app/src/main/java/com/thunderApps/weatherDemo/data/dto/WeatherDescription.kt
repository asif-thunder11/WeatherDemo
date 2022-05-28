package com.thunderApps.weatherDemo.data.dto

data class WeatherDescription(
    var id: String,
    var main: String,
    var description: String,
    var icon: String
) {
    fun getIconUrl(): String = "https://openweathermap.org/img/wn/${icon}@2x.png"
}
