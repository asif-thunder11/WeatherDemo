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

    fun Context.showToast(msg: String, type: ToastType = ToastType.TOAST_INFO, duration: Int = Toast.LENGTH_SHORT) {

        val toastView = LayoutCustomToastBinding.inflate(LayoutInflater.from(this))
        toastView.textToastMsg.text = msg
//        Toast.makeText(context, msg, duration).show()
        var toastImage: Int = R.drawable.ic_info
        when (type) {
            ToastType.TOAST_SUCCESS -> toastImage = R.drawable.ic_tick
            ToastType.TOAST_ERROR -> toastImage = R.drawable.ic_cross_round
            ToastType.TOAST_WARNING -> toastImage = R.drawable.ic_warning
        }
        toastView.let {
            it.imageTick.setImageResource(toastImage)
            it.imageTick.toVisible()
        }
        val toast = Toast(this).also {
            it.duration = duration
            it.setGravity(Gravity.BOTTOM, 0, dpToPx(16f, this))
            it.view = toastView.root
        }
        toast.show()

    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    fun View.toVisible() {
        this.visibility = View.VISIBLE
    }

    fun View.toGone() {
        this.visibility = View.GONE
    }

    fun View.toInvisible() {
        this.visibility = View.INVISIBLE
    }

    fun View.toVisibleAnimated() {
        if (this.visibility == View.VISIBLE) return
        this.apply {
            alpha = 0f
            this.toVisible()
            animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null)
        }
    }

    fun View.toGoneAnimated() {
        if (this.visibility == View.GONE) return
        this.apply {
            animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        toGone()
                    }
                })
        }
    }

    fun ImageView.loadFromUrl(url: String) {
        Glide.with(this.context)
            .load(Uri.parse(url))
            .into(this)
    }

    fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val gplayAvailability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        if (gplayAvailability == ConnectionResult.SERVICE_DISABLED || gplayAvailability == ConnectionResult.SERVICE_INVALID) {
            return false
        }
        return true
    }
}