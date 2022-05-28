package com.thunderApps.weatherDemo.data.repository

import com.thunderApps.weatherDemo.data.db.AppDB
import com.thunderApps.weatherDemo.data.dto.WeatherDataResponse
import com.thunderApps.weatherDemo.data.entity.WeatherEntity
import com.thunderApps.weatherDemo.domain.repository.LocalRepositorySource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRepo @Inject constructor(private val db: AppDB): LocalRepositorySource {

    private val weatherDao = db.weatherDao()

    override suspend fun getWeatherDetailsFromDb(): List<WeatherEntity> {
//        val myFlow: Flow<WeatherDataResponse> = flow {
//            weatherDao.getAll().collect {
//                this.emit(it.toWeatherDataResponse())
//            }
//        }
        return weatherDao.getAll()
    }

    override suspend fun saveWeatherDetailsToDb(weatherDataResponse: WeatherEntity) {
        weatherDao
    }
}