package com.thunderApps.weatherDemo.domain.repository

import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.data.entity.WeatherEntity

/**Common repository that defines methods for handling local data*/
interface LocalRepositorySource {
    suspend fun getWeatherDetailsFromDb(): List<WeatherApiResponse>         // TODO replace WeatherApiResponse with Domain layer model
    suspend fun saveWeatherDetailsToDb(weatherDataResponse: WeatherApiResponse)
}