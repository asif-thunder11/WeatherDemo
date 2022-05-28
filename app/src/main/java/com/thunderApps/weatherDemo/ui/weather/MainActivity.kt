package com.thunderApps.weatherDemo.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.thunderApps.weatherDemo.R
import com.thunderApps.weatherDemo.WeatherApp
import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.databinding.ActivityMainBinding
import com.thunderApps.weatherDemo.ui.adapter.WeatherAdapter
import com.thunderApps.weatherDemo.util.LocationHelper
import com.thunderApps.weatherDemo.util.LocationHelperListener
import com.thunderApps.weatherDemo.util.Util
import com.thunderApps.weatherDemo.util.Util.showToast
import com.thunderApps.weatherDemo.util.Util.toGone
import com.thunderApps.weatherDemo.util.Util.toGoneAnimated
import com.thunderApps.weatherDemo.util.Util.toVisible
import com.thunderApps.weatherDemo.util.Util.toVisibleAnimated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationHelperListener {

    private val TAG = "MainActivity"

    private val viewModel: MainVM by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        toolbar()
        initViews()
        locationHelper = LocationHelper(this, this)
        observeViewModel()
        locationHelper.checkLocationPermissionAndGetLocation()

    }

    override fun onStartLocationScan() {
        handleLoader(getString(R.string.scanning_location))
    }

    override fun onLocationFound(location: Location) {
        Log.d(TAG, "onLocationFound: $location")
        handleLoader(null)
        viewModel.fetchWeather(location.latitude.toFloat(), location.longitude.toFloat())
    }

    @SuppressLint("MissingPermission")
    override fun onLocationPermissionGranted() {
        Log.d(TAG, "onLocationPermissionGranted: ")

    }

    override fun onLocationPermissionDenied() {
        Log.d(TAG, "onLocationPermissionDenied: ")
    }

    private fun toolbar() {
        actionBar?.show()
        actionBar?.title = "WeatherNews"
    }

    private fun initViews() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)    // force light mode
        binding.weatherList.layoutManager = LinearLayoutManager(this)
        binding.weatherList.setHasFixedSize(true)
    }

    private fun observeViewModel() {
        WeatherApp.networkState.observe(this, this::handleConnectionState)
        viewModel._error.observe(this, this::handleError)
        viewModel._loader.observe(this, this::handleLoader)
        viewModel.weatherLiveData.observe(this, this::handleWeather)
    }

    private fun handleConnectionState(connected: Boolean) {
        Log.d(TAG, "handleConnectionState: Connected: $connected")
        if (connected) {
            binding.textNetworkState.text = getString(R.string.conn_online)
            binding.textNetworkState.setBackgroundColor(ContextCompat.getColor(this, R.color.green_00))
            Handler(Looper.getMainLooper()).postDelayed({ binding.textNetworkState.toGoneAnimated() }, 2000L)
            locationHelper.checkLocationPermissionAndGetLocation()
        } else {
            binding.textNetworkState.text = getString(R.string.no_internet_connection)
            binding.textNetworkState.setBackgroundColor(ContextCompat.getColor(this, R.color.red_df))
            binding.textNetworkState.toVisibleAnimated()
            if (binding.weatherList.adapter == null) {
                viewModel.showWeatherFromDb()
            }
        }
    }

    private fun handleLoader(msg: String?) {
        if (msg != null) {
            binding.loadingText.text = msg
            binding.layoutProgress.toVisibleAnimated()
        } else {
            binding.layoutProgress.toGoneAnimated()
        }
    }

    private fun handleError(throwable: Throwable) {
        showToast(throwable.message?:"Something Went wrong")
    }

    private fun handleWeather(weather: List<WeatherApiResponse>) {
        Log.d(TAG, "handleWeather: weather: $weather")
        binding.weatherList.adapter = WeatherAdapter(weather) { weather, pos ->

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                Log.d(TAG, "onOptionsItemSelected: save")
                locationHelper.checkLocationPermissionAndGetLocation()
            }
        }
        return true
    }

}