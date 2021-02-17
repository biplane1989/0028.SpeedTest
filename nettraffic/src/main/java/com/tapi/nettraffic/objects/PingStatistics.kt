package com.tapi.nettraffic.objects

data class PingStatistics(val icmpPackets: List<ICMPReply>)

fun PingStatistics.countPacketReceived(): Int {
    return icmpPackets.size - icmpPackets.filter { it.isPacketLost() }.size
}


fun PingStatistics.countLostPacket(): Int {
    return icmpPackets.filter { it.isPacketLost() }.size
}

fun PingStatistics.timeAvg(): Float {
    return icmpPackets.fold(0f, { acc, next -> acc + next.timeInMs }) / icmpPackets.size
}