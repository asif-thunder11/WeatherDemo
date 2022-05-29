package com.thunderApps.weatherDemo.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.thunderApps.weatherDemo.R
import com.thunderApps.weatherDemo.databinding.LayoutCustomToastBinding

object Util {

    fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val gplayAvailability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        if (gplayAvailability == ConnectionResult.SERVICE_DISABLED || gplayAvailability == ConnectionResult.SERVICE_INVALID) {
            return false
        }
        return true
    }
}