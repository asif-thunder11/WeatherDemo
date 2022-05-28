package com.thunderApps.weatherDemo.data.remote.api

import com.thunderApps.weatherDemo.Constants
import com.thunderApps.weatherDemo.data.dto.WeatherDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface WeatherApi {

    @GET(".")
    suspend fun getWeatherData(@Query("lat") lat: Float,
                               @Query("lon") lon: Float,
                               @Query("appid") appid: String = Constants.openWeatherApiKey): Response<WeatherDataResponse>
}