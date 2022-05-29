package com.thunderApps.weatherDemo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

/** Helper class which keeps track of network state and notifies listeners: [ConnectionStateListener] */
class ConnectionHelper(private val appContext: Context, val listener: ConnectionStateListener) {
    private val TAG = "ConnectionHelper"
    private val connectionManager: ConnectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val receiver = ConnectivityReceiver()
    private var currentState: Boolean = connectionManager.activeNetworkInfo?.isConnected?:false

    init {
        listener.initialState(currentState)
    }

    private inner class ConnectivityReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val networkInfo = connectionManager.activeNetworkInfo
            val fallbackNetworkInfo: NetworkInfo? = intent?.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO)

            val state: Boolean =
                if (networkInfo?.isConnected == true) {
                    true
                } else fallbackNetworkInfo?.isConnected == true

            if (currentState != state) {
                listener.onNetworkStateChange(state)
                currentState = state
            }
        }
    }

    fun startListening() {
        Log.d(TAG, "startListening: ")
        appContext.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun stopListening() {
        Log.d(TAG, "stopListening: ")
        appContext.unregisterReceiver(receiver)
    }

    fun getCurrentState(): Boolean {
        return currentState
    }
}

/** Listener for subscribing to network state published by [ConnectionHelper] */
interface ConnectionStateListener {
    fun initialState(isConnected: Boolean)
    fun onNetworkStateChange(isConnected: Boolean)
}