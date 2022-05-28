package com.thunderApps.weatherDemo.di

import com.thunderApps.weatherDemo.data.db.AppDB
import com.thunderApps.weatherDemo.data.remote.api.WeatherApi
import com.thunderApps.weatherDemo.data.repository.LocalRepo
import com.thunderApps.weatherDemo.data.repository.RemoteRepo
import com.thunderApps.weatherDemo.domain.repository.LocalRepositorySource
import com.thunderApps.weatherDemo.domain.repository.RemoteRepositorySource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideLocalRepo(db: AppDB): LocalRepositorySource {
        return LocalRepo(db)
    }

    @Singleton
    @Provides
    fun provideRemoteRepo(weatherApi: WeatherApi): RemoteRepositorySource {
        return RemoteRepo(weatherApi)
    }
}