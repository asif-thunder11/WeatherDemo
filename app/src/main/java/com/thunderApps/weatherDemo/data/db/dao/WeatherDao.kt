package com.thunderApps.weatherDemo.data.db.dao

import androidx.room.*
import com.thunderApps.weatherDemo.data.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM WeatherEntity")
    suspend fun getAll(): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(weatherEntity: WeatherEntity)

    @Delete
    suspend fun delete(weatherEntity: WeatherEntity)

    @Query("DELETE FROM WeatherEntity")
    suspend fun deleteAll()
}