package com.thunderApps.weatherDemo.domain.repository

import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.data.entity.WeatherEntity

/** Common repository that defines methods for handling data*/
interface LocalRepositorySource {
    suspend fun getWeatherDetailsFromDb(): List<WeatherApiResponse>
    suspend fun saveWeatherDetailsToDb(weatherDataResponse: WeatherApiResponse)
}