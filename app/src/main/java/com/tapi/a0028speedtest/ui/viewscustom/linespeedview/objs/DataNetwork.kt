package com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs

data class DataNetwork(var percent: Float = 0f, var transferRate: Float = 0f) {
    override fun toString(): String {
        return "percent: $percent  - transferRateBit: $transferRate"
    }
}
