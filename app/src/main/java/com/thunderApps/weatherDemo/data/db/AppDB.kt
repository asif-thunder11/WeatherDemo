package com.thunderApps.weatherDemo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thunderApps.weatherDemo.data.db.dao.WeatherDao
import com.thunderApps.weatherDemo.data.dto.WeatherData
import com.thunderApps.weatherDemo.data.dto.WeatherDataResponse
import com.thunderApps.weatherDemo.data.dto.WeatherDescription
import com.thunderApps.weatherDemo.data.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = AppDB.DB_VERSION)
abstract class AppDB : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        const val DB_VERSION: Int = 1
        const val DB_NAME: String = "WeatherDB"
    }
}