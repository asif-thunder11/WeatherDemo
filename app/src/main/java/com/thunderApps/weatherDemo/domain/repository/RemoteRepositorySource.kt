package com.thunderApps.weatherDemo.domain.repository

import com.thunderApps.weatherDemo.data.dto.WeatherDataResponse

/** Common repository that defines methods for handling remote data*/
interface RemoteRepositorySource {

    suspend fun fetchWeatherData(lat: Float, lon: Float): WeatherDataResponse

}