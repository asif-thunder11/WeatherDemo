package com.thunderApps.weatherDemo.data.error

class SomethingWentWrong(msg: String = "Something went wrong", cause : Throwable? = null) : RuntimeException(msg, cause) {
}