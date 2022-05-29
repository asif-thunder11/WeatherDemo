package com.thunderApps.weatherDemo

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.util.DateTimeUtil.toFormattedString_MMM_dd_h_mm
import org.junit.Test
import java.time.LocalDateTime
import java.time.OffsetDateTime

/** Class for performing tests of common tasks */
class GeneralTest {
    private val TAG = "GeneralTest"

    @Test
    fun parseWeatherResponse() {
        val responseString = "{\"coord\":{\"lon\":73.2043,\"lat\":22.3008},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"base\":\"stations\",\"main\":{\"temp\":305.15,\"feels_like\":309.63,\"temp_min\":305.15,\"temp_max\":305.15,\"pressure\":1006,\"humidity\":58},\"visibility\":6000,\"wind\":{\"speed\":4.12,\"deg\":250,\"gust\":9.26},\"clouds\":{\"all\":40},\"dt\":1653713452,\"sys\":{\"type\":1,\"id\":9060,\"country\":\"IN\",\"sunrise\":1653697418,\"sunset\":1653745532},\"timezone\":19800,\"id\":1253573,\"name\":\"Vadodara\",\"cod\":200}"
        val moshi = getMoshi()

        try {
            val weatherApiResponse = moshi.adapter<WeatherApiResponse>(WeatherApiResponse::class.java).fromJson(responseString)
            if (weatherApiResponse!!.cod == 200) {
                println("Response: $weatherApiResponse")
                assert(true)
            }

        } catch (e: Exception) {
            println("parseWeatherResponse: exception ${e.printStackTrace()}")
            assert(false)
        }
    }

    @Test
    fun dateFromDtEpoch() {
        val dt = 1653794807L
        val date = LocalDateTime.ofEpochSecond(dt, 0, OffsetDateTime.now().offset)
        println(date)
        println(date.toFormattedString_MMM_dd_h_mm())
        assert(true)
    }

    private fun getMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
}