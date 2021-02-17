package com.tapi.nettraffic.objects

const val DEFAULT_TIME_TO_NOT_REACH_DEST = 120000f
val ICMP_NOT_REACH_TO_DEST = ICMPReply(-1, DEFAULT_TIME_TO_NOT_REACH_DEST)

data class ICMPReply(val ttlInMs: Int, val timeInMs: Float)

fun ICMPReply.isPacketLost(): Boolean {
    return this === ICMP_NOT_REACH_TO_DEST
}

fun makeICMPNotReachToDest(times: Int): List<ICMPReply> {
    val packets = ArrayList<ICMPReply>()
    for (i in 1..times) {
        packets.add(ICMP_NOT_REACH_TO_DEST)
    }
    return packets
}
