package com.tapi.a0028speedtest.functions.common

import kotlin.math.cos

class BounceInterpolatorClass : android.view.animation.Interpolator {


    private var mAmplitude: Double = 0.0
    private var mFrequency: Double = 0.0

    constructor() {}

    constructor(mAmplitude: Double, mFrequency: Double) {
        this.mAmplitude = mAmplitude
        this.mFrequency = mFrequency

    }

    override fun getInterpolation(input: Float): Float {

        return (-1 * Math.pow(
            Math.E,
            -input / mAmplitude
        ) * cos(mFrequency * input) + 1).toFloat()

    }


}