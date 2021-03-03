package com.tapi.a0028speedtest.ui.viewscustom.progressview

import android.animation.ValueAnimator
import android.graphics.*
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.tapi.a0028speedtest.R

class BorderDrawer(val connectView: ProgressView) {
    private val mLeftPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRightPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    val drawingRect = RectF(0f, 0f, 0f, 0f)
    private val borderRect = RectF(0f, 0f, 0f, 0f)
    private val startLeftAngle = 270f
    private var startRightAngle = 90f
    private var mStrokeWidth = 0f
    private var canvasRotateValue = 0f
    private var mRotationAnimation: ValueAnimator? = null

    init {
        mLeftPaint.style = Paint.Style.STROKE
        mLeftPaint.strokeCap = Paint.Cap.ROUND
        mRightPaint.style = Paint.Style.STROKE
        mRightPaint.strokeCap = Paint.Cap.ROUND


    }


    fun onSizeChanged() {
        drawingRect.left = connectView.paddingLeft.toFloat()
        drawingRect.right =
            connectView.width - (connectView.paddingLeft + connectView.paddingRight).toFloat()
        drawingRect.top = connectView.paddingTop.toFloat()
        drawingRect.bottom =
            connectView.height - (connectView.paddingTop + connectView.paddingBottom).toFloat()
        val color1 = ContextCompat.getColor(connectView.context, R.color.white)
        val color2 = ContextCompat.getColor(connectView.context, R.color.white)
        val color11 = ContextCompat.getColor(connectView.context, R.color.colorProgress)
        val color21 = ContextCompat.getColor(connectView.context, R.color.colorProgress)

        val rightShader = LinearGradient(
            drawingRect.centerX(),
            drawingRect.top,
            drawingRect.centerX(),
            drawingRect.bottom,
            color1,
            color11,
            Shader.TileMode.MIRROR
        )

        val leftShader = LinearGradient(
            drawingRect.centerX(),
            drawingRect.top,
            drawingRect.centerX(),
            drawingRect.bottom,
            color21,
            color2,
            Shader.TileMode.MIRROR
        )
        mLeftPaint.shader = leftShader
        mRightPaint.shader = rightShader
        changeStrokeWidth(mStrokeWidth)

    }

    fun changeStrokeWidth(strokeWidth: Float) {
        mStrokeWidth = strokeWidth
        mLeftPaint.strokeWidth = mStrokeWidth
        mRightPaint.strokeWidth = mStrokeWidth
        borderRect.left = drawingRect.left + mStrokeWidth * 2
        borderRect.right = drawingRect.right - mStrokeWidth * 2

        borderRect.top = drawingRect.top + mStrokeWidth * 2
        borderRect.bottom = drawingRect.bottom - mStrokeWidth * 2
    }

    fun start() {
        mRotationAnimation?.cancel()
        mRotationAnimation = ValueAnimator.ofFloat(0f, 360f)
        mRotationAnimation?.apply {
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            duration = 2000
            addUpdateListener {
                canvasRotateValue = it.animatedValue as Float
                connectView.invalidate()
            }
            start()
        }

    }

    fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.rotate(canvasRotateValue, drawingRect.centerX(), drawingRect.centerY())
        canvas.drawArc(borderRect, startLeftAngle, 180f, false, mLeftPaint)

        canvas.drawArc(borderRect, startRightAngle, 180f, false, mRightPaint)

        canvas.restore()
    }

    fun onRelease() {
        mRotationAnimation?.cancel()
    }

}