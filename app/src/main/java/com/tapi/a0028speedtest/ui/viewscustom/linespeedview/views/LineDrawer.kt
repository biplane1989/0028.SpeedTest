package com.tapi.a0028speedtest.ui.viewscustom.linespeedview.views

import android.graphics.*
import android.util.Log
import com.tapi.a0028speedtest.functions.main.objs.Constance
import com.tapi.a0028speedtest.functions.main.objs.Vector
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs.LineChartData
import com.tapi.nettraffic.NetworkRate
import kotlin.math.pow


class LineDrawer(val lineView: LineCharView) {


    private lateinit var oldPointDownload: PointF
    private lateinit var newPointDownload: PointF
    private val mPathRunningDownload = Path()

    private var maxTransferRate = -1f
    private var ratioSmooth = Constance.DEFAULT_SMOOTH
    private var densityScreen = Constance.DEFAULT_DENSITY_VALUE

    private lateinit var oldMiddlePointDownload: PointF
    private lateinit var newMiddlePointDownload: PointF

    private lateinit var lineChartDataDownload: LineChartData
    var colorPaintDownload = Color.parseColor("#F44336")


    private lateinit var oldPointUpload: PointF
    private lateinit var newPointUpload: PointF
    private val mPathRunningUpload = Path()
    private var maxTransferRateUpload = -1f
    private lateinit var oldMiddlePointUpload: PointF
    private lateinit var newMiddlePointUpload: PointF
    var colorPaintUpload = Color.GREEN

    private lateinit var lineChartDataUpload: LineChartData
    var mWidth = 0f
    var mHeight = 0f


    private val mPaintPathDownload = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }

    private val mPaintPathUpload = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }

    fun init() {
        lineChartDataDownload = LineChartData()
        lineChartDataDownload.setGap(Constance.DEFAULT_GAP)

        lineChartDataUpload = LineChartData()
        lineChartDataUpload.setGap(Constance.DEFAULT_GAP)

    }


    fun onSizeChange() {
        mWidth = lineView.width.toFloat()
        mHeight = lineView.height.toFloat()
        lineChartDataDownload.setMaxYAxis(mHeight)
        lineChartDataUpload.setMaxYAxis(mHeight)
    }


    fun onDraw(canvas: Canvas) {
        mPaintPathDownload.color = colorPaintDownload
        mPaintPathUpload.color = colorPaintUpload
        updateLinePathDownload()
        updateLinePathUpload()
        canvas.drawPath(mPathRunningDownload, mPaintPathDownload)
        canvas.drawPath(mPathRunningUpload, mPaintPathUpload)
    }

    fun startDrawLineDownload(rate: Float, percent: Float) {
        maxTransferRate = setMaxTransferRate(rate)
       
        val point = PointF((percent) * mWidth, rate)
        Log.d("startDrawLineDownload", "checkPointX: $point  randomrate    percent $percent")

        lineChartDataDownload.setMaxYAxis(mHeight)
        lineChartDataDownload.addPoint(point.x, point.y)
        lineView.invalidate()


    }





    fun startDrawLineUpload(rate: Float, percent: Float) {
        maxTransferRateUpload = setMaxTransferRate(rate)
        /*     val point = PointF(
                 percent * mWidth / densityScreen,
                 data
             )  */
       
        val point = PointF(
            (percent ) * mWidth,
            rate
        )

        Log.d("startDrawLineUpload", "checkPointX: $point  randomrate ")

        lineChartDataUpload.setMaxYAxis(mHeight)
        lineChartDataUpload.addPoint(point.x, point.y)
        lineView.invalidate()

    }
    fun startDrawLineDownload(listData: List<NetworkRate>) {
        for (i in listData.indices) {
            maxTransferRate = setMaxTransferRate(listData[i].rate)
           
//            val posX = Utils.convertValue(0f, densityScreen.toFloat(), 0f, mWidth, listData[i].percent)
            val posX = (listData[i].percent ) * mWidth
            val point = PointF(posX, listData[i].rate)

            lineChartDataDownload.setMaxYAxis(mHeight)
            lineChartDataDownload.addPoint(point.x, point.y)
        }
        lineView.invalidate()

    }

    fun startDrawLineUpload(listData: List<NetworkRate>) {
        for (i in listData.indices) {
            maxTransferRateUpload = setMaxTransferRate(listData[i].rate)
           
            val posX = (listData[i].percent ) * mWidth
            val point = PointF(posX, listData[i].rate)
            
            lineChartDataUpload.setMaxYAxis(mHeight)
            lineChartDataUpload.addPoint(point.x, point.y)
        }
        lineView.invalidate()

    }

    private fun updateLinePathDownload() {
        mPathRunningDownload.reset()
        if (lineChartDataDownload.size() > 0) {

            mPathRunningDownload.moveTo(0f, lineChartDataDownload.getMaxYAxis())
            newPointDownload = PointF(0f, lineChartDataDownload.getMaxYAxis())
            oldMiddlePointDownload = PointF(0f, lineChartDataDownload.getMaxYAxis())

            lineChartDataDownload.forEach {
                oldPointDownload = PointF(newPointDownload.x, newPointDownload.y)
                newPointDownload = PointF(it[0], it[1])

                newMiddlePointDownload = PointF(
                    (newPointDownload.x - oldPointDownload.x) / ratioSmooth + oldPointDownload.x,
                    findYCoordinatis(
                        oldPointDownload,
                        newPointDownload,
                        (newPointDownload.x - oldPointDownload.x) / ratioSmooth + oldPointDownload.x
                    )
                )
                mPathRunningDownload.cubicTo(
                    oldMiddlePointDownload.x,
                    oldMiddlePointDownload.y,
                    oldPointDownload.x,
                    oldPointDownload.y,
                    newMiddlePointDownload.x,
                    newMiddlePointDownload.y
                )
                oldMiddlePointDownload =
                    PointF(newMiddlePointDownload.x, newMiddlePointDownload.y)

            }
        }
    }

    private fun updateLinePathUpload() {
        Log.d("TAG", "updateLinePath: upload   $lineChartDataUpload")
        mPathRunningUpload.reset()
        if (lineChartDataUpload.size() > 0) {

            mPathRunningUpload.moveTo(0f, lineChartDataUpload.getMaxYAxis())
            newPointUpload = PointF(0f, lineChartDataUpload.getMaxYAxis())
            oldMiddlePointUpload = PointF(0f, lineChartDataUpload.getMaxYAxis())
            lineChartDataUpload.forEach {

                oldPointUpload = PointF(newPointUpload.x, newPointUpload.y)
                newPointUpload = PointF(it[0], it[1])
                newMiddlePointUpload = PointF(
                    (newPointUpload.x - oldPointUpload.x) / ratioSmooth + oldPointUpload.x,
                    findYCoordinatis(
                        oldPointUpload,
                        newPointUpload,
                        (newPointUpload.x - oldPointUpload.x) / ratioSmooth + oldPointUpload.x
                    )
                )
                mPathRunningUpload.cubicTo(
                    oldMiddlePointUpload.x,
                    oldMiddlePointUpload.y,
                    oldPointUpload.x,
                    oldPointUpload.y,
                    newMiddlePointUpload.x,
                    newMiddlePointUpload.y
                )

                oldMiddlePointUpload = PointF(newMiddlePointUpload.x, newMiddlePointUpload.y)
            }
        }
    }


    private fun findYCoordinatis(oldPoint: PointF, newPoint: PointF, xPos: Float): Float {
        //directionVector
//        val directionVector = Vector(p2.x - p1.x, p2.y - p1.y)

        //normalVector
        val normalVector = Vector(x = oldPoint.y - newPoint.y, y = newPoint.x - oldPoint.x)

        return if (((oldPoint.y - newPoint.y).toDouble()
                .pow(2) + (newPoint.x - oldPoint.x).toDouble()
                .pow(2)) > 0
        ) {
            (((normalVector.x * oldPoint.x) + (normalVector.y * oldPoint.y)) - (normalVector.x * xPos)) / normalVector.y
        } else
            -1f
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

    fun setGapDownload(ratio: Int) {
        lineChartDataDownload.setGap(ratio)
    }

    fun setGap(ratio: Int) {
        lineChartDataDownload.setGap(ratio)
        lineChartDataUpload.setGap(ratio)
    }

    fun setGapUpload(ratio: Int) {
        lineChartDataUpload.setGap(ratio)
    }

    fun setDensityScreen(density: Int) {
        this.densityScreen = density
    }

    fun getDensity(): Int {
        return this.densityScreen
    }


    fun setColorLineDownload(color: Int) {
        this.colorPaintDownload = color
    }

    fun setColorLineUpload(color: Int) {
        this.colorPaintUpload = color
    }


    fun resetLineChar(typeLineView: TypeLineView) {
        when (typeLineView) {
            TypeLineView.ALL -> {
                resetLineCharDownload()
                resetLineCharUpload()
            }
            TypeLineView.DOWNLOAD -> {
                resetLineCharDownload()
            }
            TypeLineView.UPLOAD -> {
                resetLineCharUpload()
            }

        }
    }

    private fun resetLineCharDownload() {
        maxTransferRate = -1f
        lineChartDataDownload.reset()
        mPathRunningDownload.reset()
        lineView.invalidate()
    }

    private fun resetLineCharUpload() {
        maxTransferRateUpload = -1f
        lineChartDataUpload.reset()
        mPathRunningUpload.reset()
        lineView.invalidate()
    }


}