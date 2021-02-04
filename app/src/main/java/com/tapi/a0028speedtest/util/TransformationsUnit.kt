package com.tapi.a0028speedtest.util


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class TransformationsUnit {
    companion object {
        fun <X, Y, Z> map(
            sourceX: LiveData<X>,
            sourceY: LiveData<Y>,
            mapFunction: (x: X?, y: Y?) -> Z
        ): LiveData<Z> {
            val result = MediatorLiveData<Z>()
            result.addSource(sourceX) { x ->
                result.value = mapFunction(x, sourceY.value)
            }
            result.addSource(sourceY) { y ->
                result.value = mapFunction(sourceX.value, y)
            }
            return result
        }
    }
}