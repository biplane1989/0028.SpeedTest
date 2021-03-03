package com.tapi.a0028speedtest.ui.viewscustom.progressview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tapi.a0028speedtest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProgressView( val mContext: Context, attrs: AttributeSet?) :
    View(mContext, attrs) {
    private val mpaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderDrawer = BorderDrawer(this)

    private val typeArray =
        context.theme.obtainStyledAttributes(attrs, R.styleable.ConnectViewStyleable, 0, 0)

    init {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            startAnim()
        }
        borderDrawer.changeStrokeWidth(5f)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        borderDrawer.onDraw(canvas)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        borderDrawer.onSizeChanged()
    }

    private fun setColorPaint(context: Context) {
        val colors = arrayListOf<Int>()
        colors.add(ContextCompat.getColor(context, R.color.colorBlueAlpha))
        colors.add(0)
        colors.add(ContextCompat.getColor(context, R.color.colorCircleCenterBackround))
        val positions = FloatArray(3)
        positions[0] = 0f
        positions[1] = 0.2f
        positions[2] = 0.6f
        val gradient = SweepGradient(
            width / 2f,
            height / 2f,
            colors.toIntArray(),
            positions
        )
        val matrix = Matrix()
        matrix.setRotate(180f, 0f, height / 2f)
        gradient.setLocalMatrix(matrix)
        mpaint.shader = gradient
        mpaint.style = Paint.Style.FILL
    }



    suspend fun startAnim() {
        borderDrawer.start()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        borderDrawer.onRelease()
    }


}