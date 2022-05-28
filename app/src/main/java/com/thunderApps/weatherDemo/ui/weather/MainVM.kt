package com.thunderApps.weatherDemo.ui.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.data.error.ApiException
import com.thunderApps.weatherDemo.data.error.SomethingWentWrong
import com.thunderApps.weatherDemo.domain.repository.LocalRepositorySource
import com.thunderApps.weatherDemo.domain.repository.RemoteRepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    val localRepo: LocalRepositorySource,
    val remoteRepo: RemoteRepositorySource
) : ViewModel() {

    private val TAG = "MainVM"

    /** A live data to observe errors from ViewModel */
    val _error = MutableLiveData<Throwable>()

    /** LiveData for controlling main loading ui. Supports description*/
    val _loader = MutableLiveData<String?>()
    val weatherLiveData = MutableLiveData<List<WeatherApiResponse>>()

    fun fetchWeather(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            _loader.postValue("Fetching Weather Data...")
            try {
                val weatherApiResponse = remoteRepo.fetchWeatherData(lat, lon)
                localRepo.saveWeatherDetailsToDb(weatherApiResponse)                // save to local db
                weatherLiveData.postValue(localRepo.getWeatherDetailsFromDb())      // getAll from localDb and post the list
            } catch (e: Exception) {
                Log.e(TAG, "fetchWeather: exception: ", e)
                onError(ApiException("Something went wrong.\nFailed to fetch Weather Data."))
            } finally {
                _loader.postValue(null)
            }
        }
    }

    fun showWeatherFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherLiveData.postValue(localRepo.getWeatherDetailsFromDb())
            } catch (e: Exception) {
                onError(SomethingWentWrong())
            }

        }

    }

    private fun onError(throwable: Throwable) {
        Log.d(TAG, "onError: ${throwable.printStackTrace()}")
        _error.postValue(throwable)
    }

}