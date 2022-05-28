package com.thunderApps.weatherDemo.data.dto


data class WeatherDataResponse constructor(
    var lat: Float,
    var lon: Float,
    var timezone: String,
    var timezone_offset: Long,
    var current: WeatherData,
)

data class WeatherData(
    var dt: Long,
    var temp: Float,
    var weather: List<WeatherDescription>
)

data class WeatherDescription(
    var id: String,
    var main: String,
    var description: String,
    var icon: String
) {
    fun getIconUrl(): String = "http://openweathermap.org/img/wn/${icon}@2x.png"
}
