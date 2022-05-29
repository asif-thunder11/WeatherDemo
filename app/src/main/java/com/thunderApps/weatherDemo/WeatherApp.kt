package com.thunderApps.weatherDemo

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import androidx.lifecycle.ProcessLifecycleOwner
import com.squareup.moshi.Moshi
import com.thunderApps.weatherDemo.util.ConnectionHelper
import com.thunderApps.weatherDemo.util.ConnectionStateListener
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp : Application(), ConnectionStateListener, LifecycleEventObserver {
    private val TAG = "WeatherApp"

    private lateinit var connectionHelper: ConnectionHelper

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)    // force light mode
        connectionHelper = ConnectionHelper(applicationContext, this)
        connectionHelper.startListening()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun initialState(isConnected: Boolean) {
        Log.d(TAG, "initialState: $isConnected")
        _networkState.postValue(isConnected)
    }

    override fun onNetworkStateChange(isConnected: Boolean) {
        Log.d(TAG, "onStateChange: $isConnected")
        _networkState.postValue(isConnected)
    }

    // lifecycle state change
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Log.d(TAG, "onLifecycleStateChanged: ${event.name}")
    }

    companion object {
        lateinit var moshi: Moshi
        private val _networkState = MutableLiveData<Boolean>()
        val networkState = _networkState as LiveData<Boolean>
    }
}