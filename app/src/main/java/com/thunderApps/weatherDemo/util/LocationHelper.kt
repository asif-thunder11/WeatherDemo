package com.thunderApps.weatherDemo.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
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
import com.thunderApps.weatherDemo.util.Util.showToast

/** Helper class for location permission management and getting current location */
class LocationHelper(private val activity: AppCompatActivity, val listener: LocationHelperListener) {
    private val TAG = "LocationHelper"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestLocationLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean -> if (isGranted) onLocationPermissionGranted() else onLocationPermissionDenied()
    }

    init {

    }

    fun checkLocationPermissionAndGetLocation() {
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
        listener.onLocationPermissionGranted()

        Log.d(TAG, "onLocationPermissionGranted: ")
        // TODO check if location is enabled

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
                if (it != null) {
                    Log.d(TAG, "onLocationPermissionGranted: ${it.latitude}, ${it.longitude}")
                    listener.onLocationFound(it)
                } else {

                }
            }

            fusedLocationClient.lastLocation.addOnFailureListener {
                Log.d(TAG, "onLocationPermissionGranted: getLocation: Failed")
            }
        }
    }

    private fun onLocationPermissionDenied() {
        listener.onLocationPermissionDenied()
    }

    fun showLocationPermissionRequiredDialog() {
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


}

interface LocationHelperListener {
    fun onLocationPermissionGranted()
    fun onLocationPermissionDenied()
    fun onStartLocationScan()
    fun onLocationFound(location: Location)
}