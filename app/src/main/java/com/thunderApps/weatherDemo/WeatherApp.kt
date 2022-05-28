package com.thunderApps.weatherDemo

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import com.thunderApps.weatherDemo.util.ConnectionHelper
import com.thunderApps.weatherDemo.util.ConnectionStateListener
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltAndroidApp
class WeatherApp : Application(), ConnectionStateListener {
    private val TAG = "WeatherApp"

    private lateinit var connectionHelper: ConnectionHelper
    val networkStateLiveData = MutableLiveData<Boolean>()

    override fun onCreate() {
        super.onCreate()
        connectionHelper = ConnectionHelper(applicationContext, this).apply { startListening() }
    }

    override fun initialState(isConnected: Boolean) {
        networkStateLiveData.postValue(isConnected)
    }

    override fun onStateChange(isConnected: Boolean) {
        Log.d(TAG, "onStateChange: $isConnected")
        networkStateLiveData.postValue(isConnected)
    }

    companion object {
        lateinit var moshi: Moshi
    }
}