package com.thunderApps.weatherDemo.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.thunderApps.weatherDemo.R
import com.thunderApps.weatherDemo.ui.util.UiUtil.showToast

/** Helper class for location permission management and getting current location */
class LocationHelper(private val activity: AppCompatActivity, val listener: LocationHelperListener) {
    private val TAG = "LocationHelper"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestLocationLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean -> if (isGranted) onLocationPermissionGranted() else onLocationPermissionDenied()
    }

    private var locationDisabledDialog: AlertDialog? = null
    private var backFromLocationSettingScreen = false

    init {

    }

    /** Handles permission management and gets current location*/
    fun checkLocationPermissionAndGetLocation() {
        // we are only using approximate location here as per our use case
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkLocationPermission: granted")
            onLocationPermissionGranted()
        } else {
            Log.d(TAG, "checkLocationPermission: not granted")
            showLocationPermissionRequiredDialog()
        }
    }

    @SuppressLint("MissingPermission")
    private fun onLocationPermissionGranted() {
        Log.d(TAG, "onLocationPermissionGranted: ")
        listener.onLocationPermissionGranted()

        // check if GooglePlay services are enabled as we are using Google Play location api
        if (!Util.isGooglePlayServicesAvailable(activity)) {
            val alertDialog = AlertDialog.Builder(activity)
                .setTitle("Device Not Supported")
                .setMessage("Your device doesn't support Google Play Services. The app cannot proceed.")
                .setIcon(R.drawable.ic_cross_round)
                .setNeutralButton("Ok", ) { _, _  -> activity.finish() }
                .create()
            alertDialog.show()
        } else {
            Log.d(TAG, "onLocationPermissionGranted: GooglePlayServices Available")

            // check if location is enabled
            if (!getLocationState()) {
                Log.d(TAG, "onLocationPermissionGranted: Location disabled")
                listener.onLocationState(false)
                showLocationDisabledDialog()
            } else {        // getLocation only when enabled
                Log.d(TAG, "onLocationPermissionGranted: Location enabled")
                listener.onLocationState(true)
                listener.onStartLocationScan()
                if (!::fusedLocationClient.isInitialized) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
                }
                fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                        return this;
                    }

                    override fun isCancellationRequested(): Boolean {
                        return false
                    }
                }).addOnSuccessListener {
                    Log.d(TAG, "onLocationPermissionGranted: getLocation Success: location: ${it?:null}")
                    it?.let { listener.onLocationFound(it) }
                }

                fusedLocationClient.lastLocation.addOnFailureListener {
                    Log.d(TAG, "onLocationPermissionGranted: getLocation: Failed")
                }
            }
        }
    }

    private fun onLocationPermissionDenied() {
        listener.onLocationPermissionDenied()
    }

    private fun showLocationPermissionRequiredDialog() {
        // Keeping dialog UI simple for brevity
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle("Permission Required")
            .setMessage("Please allow the location permission to get accurate weather data.")//\nIf you deny, your IP address will be used to detect your location.")
            .setIcon(R.drawable.ic_info)
            .setPositiveButton("Allow") { _, _ -> requestLocationLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION) }
            .setNegativeButton("Deny") { _, _ ->  activity.showToast("The app cannot proceed without location permission")
                activity.finish()
            }
            .create()
        alertDialog.show()
    }

    fun getLocationState(): Boolean {
        val locationManager: LocationManager = activity.getSystemService(LocationManager::class.java)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // TODO Allow option of location based on IP address

    private fun showLocationDisabledDialog() {
        locationDisabledDialog = AlertDialog.Builder(activity)
            .setTitle("Location Disabled")
            .setMessage("Please enable location from settings and try again by pressing the Save button")
            .setIcon(R.drawable.ic_cross_round)
            .setPositiveButton("Ok") { d, w ->
                try {
                    activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    backFromLocationSettingScreen = true
                } catch (e: Exception) {
                    Log.e(TAG, "showLocationDisabledDialog: Failed to open location settings", e)
                }
            }
            .setNegativeButton("Cancel") { _, _ -> activity.showToast("Weather data will not be updated if location is disabled", type = ToastType.TOAST_WARNING, duration = Toast.LENGTH_LONG) }
            .create()
        locationDisabledDialog?.show()
    }

    fun onHostResume() {
        if (locationDisabledDialog?.isShowing == true && backFromLocationSettingScreen) {
            if (getLocationState()) {
                locationDisabledDialog?.dismiss()
            }
            backFromLocationSettingScreen = false
        }
    }

    fun onHostPause() {

    }

}

interface LocationHelperListener {
    fun onLocationPermissionGranted()
    fun onLocationPermissionDenied()
    fun onStartLocationScan()
    fun onLocationFound(location: Location)

    /** Notifies location state of device*/
    fun onLocationState(isEnabled: Boolean)
}