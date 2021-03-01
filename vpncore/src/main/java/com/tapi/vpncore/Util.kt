package com.tapi.vpncore

import android.location.Location

class Util {
    companion object{
        val location1 = Location("location1")
        val location2 = Location("location2")
        fun distance(lat1:Double, lon1:Double, lat2:Double, lon2:Double):Float{
            location1.latitude = lat1
            location1.longitude = lon1

            location2.latitude = lat2
            location2.longitude = lon2
            return location1.distanceTo(location2)
        }
    }
}