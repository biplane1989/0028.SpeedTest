package com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs

import android.graphics.PointF
import android.util.Log


class LineChartData : Iterable<FloatArray> {
    companion object {
        private const val MAX_Y_AXIS_DEFAULT = -1f
    }

    private var mGap: Int = 1
    private var mIndex = -1
    private val mListCoords = ArrayList<PointF>()
    private var totalPoint = 0
    private var mMaxYAxis = MAX_Y_AXIS_DEFAULT
    private var mMaxYValue = 0f

    private var xOld = -1f

    fun reset() {
        mIndex = -1
        mListCoords.clear()
        totalPoint = 0
        mMaxYValue =0f
    }

    fun clearPoint() {
        mIndex = -1
        mListCoords.clear()
        totalPoint = 0
        mMaxYAxis = MAX_Y_AXIS_DEFAULT
        mMaxYValue = 0f
    }

    fun addPoint(x: Float, y: Float) {
        if (xOld != x) {
            Log.d("TAG", "addPoint: x $x, y $y")
            totalPoint++
            if (mIndex == -1) {
                mListCoords.add(PointF(x, y))
                mIndex++
            } else {
                if (totalPoint % mGap == 0) {
                    val startIndex = size() + 1 - mGap
                    val endIndex = startIndex + mGap
                    var avgY = 0f
                    for (i in startIndex until (endIndex - 1)) {
                        avgY += mListCoords[i].y
                    }
                    avgY += y
                    avgY /= mGap
                    setOrAddNewPoint(startIndex, x, avgY)
                    mIndex = startIndex
                } else {
                    if (mIndex == (mListCoords.size - 1)) {
                        mListCoords.add(PointF(x, y))
                    } else {
                        setOrAddNewPoint(mIndex + 1, x, y)
                    }
                    mIndex++
                }
            }

            for (i in 0..mIndex) {
                if (mListCoords[i].y > mMaxYValue) {
                    mMaxYValue = mListCoords[i].y
                }
            }
            xOld = x

        }


    }

    private fun setOrAddNewPoint(index: Int, x: Float, y: Float) {
        if (index < mListCoords.size) {
            mListCoords[index].x = x
            mListCoords[index].y = y
        } else {
            mListCoords.add(PointF(x, y))
        }
    }

    fun setMaxYAxis(maxValue: Float) {
        mMaxYAxis = maxValue
    }

    fun setGap(gap: Int) {
        mGap = gap
    }

    fun size(): Int {
        return mIndex + 1
    }

    fun get(index: Int): FloatArray {
        val point = mListCoords[index]
        if (mMaxYAxis != MAX_Y_AXIS_DEFAULT && mMaxYValue > 0f) {
            return floatArrayOf(
                point.x,
                mMaxYAxis - convertValue(0f, mMaxYValue, 0f, mMaxYAxis, point.y)
            )
        } else {
            return floatArrayOf(point.x, point.y)
        }

    }

    fun getMaxYValue(): Float {
        return mMaxYValue
    }

    fun getMaxYAxis(): Float {
        return mMaxYAxis
    }

    fun convertValue(
        min1: Float,
        max1: Float,
        min2: Float,
        max2: Float,
        value: Float
    ): Float {
        return ((value - min1) * ((max2 - min2) / (max1 - min1)) + min2)
    }

    override fun iterator(): Iterator<FloatArray> {
        return object : Iterator<FloatArray> {
            var i = 0
            override fun hasNext(): Boolean {
                return i <= mIndex
            }

            override fun next(): FloatArray {
                val point = get(i)
                i++
                return point
            }
        }
    }
}
