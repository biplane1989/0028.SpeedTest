package com.tapi.a0028speedtest.ui.viewscustom.speedview.animations

import com.tapi.speedtest.ui.speedview.view.Speedometer
import com.tapi.a0028speedtest.ui.viewscustom.speedview.view.SpeedometerView
import kotlinx.coroutines.delay

class TickNumberAnimation(val arcView: SpeedometerView, val speedometer: Speedometer) {
    suspend fun start() {
//        isCanceled = false
        speedometer.endTickPosition = -1
        arcView.invalidateGauge()

        for (index in 0..speedometer.tickNumber) {
            delay(50)
            speedometer.endTickPosition = index
            arcView.setRatioAlpha(((index.toFloat() / speedometer.tickNumber) * 255).toInt())
            arcView.invalidateGauge()
        }
    }

    suspend fun startFlash() {
//        isCanceled = false
        speedometer.endTickPosition = speedometer.tickNumber
        arcView.setRatioAlpha(255)
        arcView.invalidateGauge()
    }

    fun resetView() {
        speedometer.endTickPosition = -1
        arcView.invalidateGauge()
    }


}