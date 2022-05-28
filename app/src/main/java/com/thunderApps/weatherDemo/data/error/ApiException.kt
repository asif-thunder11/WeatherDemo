package com.thunderApps.weatherDemo.data.error

class ApiException(msg:String? = "Api Error", cause: Throwable? = null) : RuntimeException(msg, cause) {
}