package com.tapi.a0028speedtest.data
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs.DataNetwork
import com.tapi.nettraffic.NetworkRate
import com.tapi.nettraffic.objects.ConnectionType
import com.tapi.vpncore.listserver.Server
import com.tapi.vpncore.objects.Host

enum class NetworkType{
    WIFI,_2G,_3G,_4G,_5G
}

data class History(val id: Int,
                   val client: Host,
                   val server: Server,
                   var downloadRate: Float,
                   var updateRate: Float,
                   val pingRate: Float,
                   val downloadTrace: List<NetworkRate>,
                   val uploadTrace: List<NetworkRate>,
                   val externalIP: String,
                   val created: Long,
                   val networkType: NetworkType,
                   val connectionType: ConnectionType
)


fun History.getDownloadRate(dataRateUnits: DataRateUnits): Float {
    when (dataRateUnits) {
        DataRateUnits.MBPS -> {
            return downloadRate /// (1024 * 1024f * 8)
        }
        DataRateUnits.MbPS -> {
            return downloadRate /// (1024f * 1024)
        }
        DataRateUnits.KBPS -> {
            return downloadRate /// (1024 * 8f)
        }
    }
}

fun History.getUpdateRate(dataRateUnits: DataRateUnits): Float {
    when (dataRateUnits) {
        DataRateUnits.MBPS -> {
            return updateRate // (1024 * 1024f * 8)
        }
        DataRateUnits.MbPS -> {
            return updateRate // (1024f * 1024)
        }
        DataRateUnits.KBPS -> {
            return updateRate // (1024 * 8f)
        }
    }
}

