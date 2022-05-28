package com.thunderApps.weatherDemo.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtil {

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd")

    fun LocalDateTime.toFormattedString_MMM_dd(): String {
        try {
            return dateFormatter.format(this)
        } catch (e: Exception) {
            return "${this.dayOfMonth}-${this.monthValue}-${this.year}"
        }
    }
}