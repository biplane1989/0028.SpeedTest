package com.tapi.a0028speedtest.functions.maintab.objs

import com.tapi.nettraffic.objects.NetworkType
import com.tapi.vpncore.Util
import com.tapi.vpncore.listserver.Server

fun Server.distance(lat:Double, lon:Double):Float{
return Util.distance(this.lat, this.lon, lat, lon)
}
data class NetWorkViewItem(val server: Server, val networkType: NetworkType, var favorite: Boolean, var distance : String, var isSelect : Boolean = false)

val NETWORK_VIEW_ITEM_FAKE = NetWorkViewItem(Server(), NetworkType.WIFI, false, "", false)
