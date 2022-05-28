package com.thunderApps.weatherDemo.data.repository

import android.util.Log
import com.thunderApps.weatherDemo.data.dto.WeatherDataResponse
import com.thunderApps.weatherDemo.data.error.ApiException
import com.thunderApps.weatherDemo.data.error.SomethingWentWrong
import com.thunderApps.weatherDemo.data.remote.api.WeatherApi
import com.thunderApps.weatherDemo.domain.repository.RemoteRepositorySource
import javax.inject.Inject

class RemoteRepo @Inject constructor(private val weatherApi: WeatherApi) : RemoteRepositorySource {
    private val TAG = "RemoteRepo"

    override suspend fun fetchWeatherData(lat: Float, lon: Float): WeatherDataResponse {
        val response = weatherApi.getWeatherData(lat, lon)
        Log.d(TAG, "fetchWeatherData: response: $response")
        if (response.isSuccessful) {
            return response.body()?:throw SomethingWentWrong("Failed to fetch weather data")
        } else {
            val errorString = String(response.errorBody()?.byteStream()?.readBytes()!!)
            throw ApiException(errorString)
        }
    }
}