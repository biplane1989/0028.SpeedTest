package com.tapi.a0028speedtest.ui.viewscustom.linespeedview.views

import android.graphics.*
import android.util.Log
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs.DataNetwork
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs.LineChartData
import com.tapi.a0028speedtest.util.Constances
import com.tapi.a0028speedtest.util.Utils


class LineDownloadDrawer(val lineView: LineCharView) {


    private lateinit var oldPoint: PointF
    private lateinit var newPoint: PointF
    private val mPathRunning = Path()

    private var maxTransferRate = -1f
    private var ratioSmooth = Constances.DEFAULT_SMOOTH
    private var densityScreen = Constances.DEFAULT_DENSITY_VALUE

    private lateinit var oldMiddlePoint: PointF
    private lateinit var newMiddlePoint: PointF

    private lateinit var lineChartData: LineChartData

    var colorPaint = Color.parseColor("#F44336")
    var mWidth = 0f
    var mHeight = 0f

    private val mPaintPath = Paint().apply {

        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }

    fun init() {
        lineChartData = LineChartData()
        lineChartData.setGap(Constances.DEFAULT_GAP)
    }


    fun onSizeChange() {
        mWidth = lineView.width.toFloat()
        mHeight = lineView.height.toFloat()
        lineChartData.setMaxYAxis(mHeight)
    }


    fun onDraw(canvas: Canvas) {
        mPaintPath.color = colorPaint
        updateLinePath()
        canvas.drawPath(mPathRunning, mPaintPath)
    }

    fun startDrawLine(data: DataNetwork) {

        maxTransferRate = setMaxTransferRate(data.transferRate)
        val point = PointF(
            data.percent * mWidth / densityScreen,
            data.transferRate
        )
        Log.d("TAG", "startDrawLine: $point")
        lineChartData.setMaxYAxis(mHeight)
        lineChartData.addPoint(point.x, point.y)
        lineView.invalidate()
        maxTransferRate = setMaxTransferRate(data.transferRate)

    }

    private fun updateLinePath() {
        mPathRunning.reset()
        if (lineChartData.size() > 0) {
//            val firstPoint = lineChartData.get(0)
//            mPathRunning.moveTo(firstPoint[0], firstPoint[1])
            mPathRunning.moveTo(0f, lineChartData.getMaxYAxis())
            newPoint = PointF(0f, lineChartData.getMaxYAxis())
            oldMiddlePoint = PointF(0f, lineChartData.getMaxYAxis())
            lineChartData.forEach {

                oldPoint = PointF(newPoint.x, newPoint.y)
                newPoint = PointF(it[0], it[1])
                newMiddlePoint = PointF(
                    (newPoint.x - oldPoint.x) / ratioSmooth + oldPoint.x,
                    Utils.findYCoordinatis(
                        oldPoint,
                        newPoint,
                        (newPoint.x - oldPoint.x) / ratioSmooth + oldPoint.x
                    )
                )
                mPathRunning.cubicTo(
                    oldMiddlePoint.x,
                    oldMiddlePoint.y,
                    oldPoint.x,
                    oldPoint.y,
                    newMiddlePoint.x,
                    newMiddlePoint.y
                )
                oldMiddlePoint = PointF(newMiddlePoint.x, newMiddlePoint.y)
            }
        }
    }

    fun setPrSmooth(ratio: Int) {
        this.ratioSmooth = ratio
    }

    private fun setMaxTransferRate(speed: Float): Float {
        if (maxTransferRate < speed) {
            return speed
        }
        return maxTransferRate
    }

    fun setGap(ratio: Int) {
        lineChartData.setGap(ratio)
    }

    fun setDensityScreen(density: Int) {
        this.densityScreen = density
    }


    fun setColorLineDownload(color: Int) {
        this.colorPaint = color
    }

    fun resetLineChar() {
        lineChartData.reset()
        mPathRunning.reset()
        lineView.invalidate()
    }

}