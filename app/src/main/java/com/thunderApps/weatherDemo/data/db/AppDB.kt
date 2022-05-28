package com.thunderApps.weatherDemo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thunderApps.weatherDemo.data.db.dao.WeatherDao
import com.thunderApps.weatherDemo.data.entity.WeatherDescriptionConverter
import com.thunderApps.weatherDemo.data.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = AppDB.DB_VERSION)
@TypeConverters(WeatherDescriptionConverter::class)
abstract class AppDB : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        const val DB_VERSION: Int = 1
        const val DB_NAME: String = "WeatherDB"
    }
}