package com.thunderApps.weatherDemo.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.thunderApps.weatherDemo.WeatherApp
import com.thunderApps.weatherDemo.data.db.AppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext ctx: Context): AppDB {
        return Room.databaseBuilder(
            ctx,
            AppDB::class.java,
           AppDB.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        WeatherApp.moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return WeatherApp.moshi
    }
}