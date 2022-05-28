package com.thunderApps.weatherDemo.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thunderApps.weatherDemo.data.dto.WeatherData
import com.thunderApps.weatherDemo.data.dto.WeatherDataResponse

@Entity
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var weatherData: String
)
