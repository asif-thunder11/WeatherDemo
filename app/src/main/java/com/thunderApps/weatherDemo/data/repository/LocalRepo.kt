package com.thunderApps.weatherDemo.data.repository

import com.thunderApps.weatherDemo.data.db.AppDB
import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.data.dto.toWeatherEntity
import com.thunderApps.weatherDemo.data.entity.WeatherEntity
import com.thunderApps.weatherDemo.data.entity.toWeatherApiResponse
import com.thunderApps.weatherDemo.domain.repository.LocalRepositorySource
import javax.inject.Inject

class LocalRepo @Inject constructor(private val db: AppDB): LocalRepositorySource {

    private val weatherDao = db.weatherDao()

    override suspend fun getWeatherDetailsFromDb(): List<WeatherApiResponse> {
        val list = arrayListOf<WeatherApiResponse>()
        weatherDao.getAll().forEach { list.add(it.toWeatherApiResponse()) }
        return list
    }

    override suspend fun saveWeatherDetailsToDb(weatherDataResponse: WeatherApiResponse) {
        weatherDao.save(weatherDataResponse.toWeatherEntity())
    }
}