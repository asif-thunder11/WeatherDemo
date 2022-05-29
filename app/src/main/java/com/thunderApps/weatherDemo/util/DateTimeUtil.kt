package com.thunderApps.weatherDemo.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtil {

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, h:mm a")

    fun LocalDateTime.toFormattedString_MMM_dd_h_mm(): String {
        try {
            return dateFormatter.format(this)
        } catch (e: Exception) {
            return "${this.dayOfMonth}-${this.monthValue}-${this.year}"
        }
    }
}