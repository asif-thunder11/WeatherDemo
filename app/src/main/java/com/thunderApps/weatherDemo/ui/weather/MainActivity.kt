package com.thunderApps.weatherDemo.ui.weather

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thunderApps.weatherDemo.R
import com.thunderApps.weatherDemo.WeatherApp
import com.thunderApps.weatherDemo.data.dto.WeatherApiResponse
import com.thunderApps.weatherDemo.databinding.ActivityMainBinding
import com.thunderApps.weatherDemo.ui.adapter.WeatherAdapter
import com.thunderApps.weatherDemo.ui.util.UiUtil.showToast
import com.thunderApps.weatherDemo.ui.util.UiUtil.toGoneAnimated
import com.thunderApps.weatherDemo.ui.util.UiUtil.toVisibleAnimated
import com.thunderApps.weatherDemo.util.LocationHelper
import com.thunderApps.weatherDemo.util.LocationHelperListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationHelperListener {

    private val TAG = "MainActivity"

    private val viewModel: MainVM by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationHelper: LocationHelper
    private lateinit var loaderSnack: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        toolbar()
        initViews()
        locationHelper = LocationHelper(this, this)
        observeViewModel()
        locationHelper.checkLocationPermissionAndGetLocation()
        viewModel.showWeatherFromDb()   // show cached data if present
    }

    override fun onStartLocationScan() {
        handleLoader(getString(R.string.scanning_location))
    }

    override fun onLocationFound(location: Location) {
        Log.d(TAG, "onLocationFound: $location")
        viewModel.fetchWeather(location.latitude.toFloat(), location.longitude.toFloat())
    }

    override fun onLocationState(isEnabled: Boolean) {
        Log.d(TAG, "onLocationState: $isEnabled")
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
    }

    private fun initViews() {
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
            Handler(Looper.getMainLooper()).postDelayed(
                { binding.textNetworkState.toGoneAnimated() },
                2000L
            )
            locationHelper.checkLocationPermissionAndGetLocation()
        } else {
            binding.textNetworkState.text = getString(R.string.no_internet_connection)
            binding.textNetworkState.setBackgroundColor(ContextCompat.getColor(this, R.color.red_df))
            binding.textNetworkState.toVisibleAnimated()
            // show local only if not already shown
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
        showToast(throwable.message ?: "Something Went wrong")
    }

    private fun handleWeather(weather: List<WeatherApiResponse>) {
        Log.d(TAG, "handleWeather: weather: $weather")
        binding.weatherList.adapter = WeatherAdapter(weather) { weather, pos ->
            // listener code goes here
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
                if (WeatherApp.networkState.value == true) {
                    locationHelper.checkLocationPermissionAndGetLocation()
                } else {
                    showToast("Please enable your internet and try again")
                }
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        locationHelper.onHostResume()
    }

    override fun onPause() {
        locationHelper.onHostPause()
        super.onPause()
    }

}