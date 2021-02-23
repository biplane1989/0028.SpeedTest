package com.tapi.a0028speedtest.ui.viewscustom.speedview.components.indicators

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import androidx.core.content.ContextCompat
import com.tapi.a0028speedtest.R

/**
 * this Library build By Anas Altair
 * see it on [GitHub](https://github.com/anastr/SpeedView)
 */
class SpindleIndicator(val context: Context) : Indicator<SpindleIndicator>(context) {
    private val indicatorPath = Path()
    private var bottomY: Float = 0.toFloat()

    init {
        width = dpTOpx(12f)
    }

    override fun getTop(): Float {
        return getViewSize() / 5f + speedometer!!.padding
    }

    override fun getBottom(): Float {
        return bottomY
    }

    @SuppressLint("ResourceAsColor")
    override fun draw(canvas: Canvas, degree: Float) {
        /**draw indicator**/
        canvas.save()
        canvas.rotate(90f + degree, getCenterX(), getCenterY())
        setColorArcPaint(context)
        canvas.drawPath(indicatorPath, indicatorPaint)
        canvas.restore()
    }

    private fun setColorArcPaint(context: Context) {
        val colors = arrayListOf<Int>()
        colors.add(ContextCompat.getColor(context, R.color.colorBlue))
        colors.add(0)
        colors.add(ContextCompat.getColor(context, R.color.colorBlue))
        val positions = FloatArray(3)
        positions[0] = 0f
        positions[1] = 0.2f
        positions[2] = 0.6f
        indicatorPaint.shader = SweepGradient(
            100f,
            100f,
            colors.toIntArray(),
            positions
        )

        indicatorPaint.style = Paint.Style.FILL
    }

    override fun updateIndicator() {
        indicatorPath.reset()

        val topSharp = getViewSize() / 5f + speedometer!!.padding+50
        indicatorPath.moveTo(getCenterX(), getCenterY())
        /**
         * change position indicator
         * */
        val x1 = getCenterX() - width
        val x2 = getCenterX() - width / 4
        val x3 = getCenterX() + width / 4
        val x4 = getCenterX() + width


        Log.d(
            "TAG",
            "updateIndicator:  getCenterX ${getCenterX()} width ${width}  x1: ${x1} x2 :${x2}   x4 ${x4}  bottomy $bottomY"
        )
        indicatorPath.lineTo(x1, getCenterX())
        indicatorPath.lineTo(x2, topSharp)
        indicatorPath.lineTo(x3, topSharp)
        indicatorPath.lineTo(x4, getCenterX())
        indicatorPath.addCircle(getCenterX(), topSharp, width / 4, Path.Direction.CW)
        indicatorPaint.color = color
    }

    override fun setWithEffects(withEffects: Boolean) {
        if (withEffects && !speedometer!!.isInEditMode) {
            indicatorPaint.maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.SOLID)
        } else {
            indicatorPaint.maskFilter = null
        }
    }
}
