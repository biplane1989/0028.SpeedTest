package com.tapi.vpncore.objects

import java.lang.Exception

data class Host(val name: String, val location: String, val lat: String, val lon: String, val ip: String, val networkProvider: String = "")

fun Host.latValue(): Double {
    try {
        return lat.toDouble()
    } catch (e: Exception) {
        return 0.0
    }

}

fun Host.lonValue(): Double {
    try {
        return lon.toDouble()
    } catch (e: Exception) {
        return 0.0
    }
}