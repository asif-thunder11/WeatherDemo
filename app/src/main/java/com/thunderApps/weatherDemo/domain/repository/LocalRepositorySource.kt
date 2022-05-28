package com.thunderApps.weatherDemo.domain.repository

import com.thunderApps.weatherDemo.data.dto.WeatherDataResponse
import com.thunderApps.weatherDemo.data.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

/** Common repository that defines methods for handling data*/
interface LocalRepositorySource {
    suspend fun getWeatherDetailsFromDb(): List<WeatherEntity>
    suspend fun saveWeatherDetailsToDb(weatherDataResponse: WeatherEntity)
}