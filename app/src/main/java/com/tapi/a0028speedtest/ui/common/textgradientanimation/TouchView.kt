package com.tapi.a0028speedtest.textgradientanimation

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatImageView

class TouchView : View {
    private var isEnableAnim = true
    private var isEnableScale = false
    private var isLongClick = true

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    fun enableAnim(anim: Boolean) {
        isEnableAnim = anim
    }

    fun enableScale(isScale: Boolean) {
        this.isEnableScale = isScale
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isEnableAnim) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isLongClick = false
                    this.alpha = 0.5f
                    if (isEnableScale) {
                        this.scaleX = 0.95f
                        this.scaleY = 0.95f
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    this.alpha = 1f
                    if (isEnableScale) {
                        this.scaleX = 1f
                        this.scaleY = 1f
                    }
                    performClick()
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    this.alpha = 1f
                    if (isEnableScale) {
                        this.scaleX = 1f
                        this.scaleY = 1f
                    }
                    true
                }
                else -> false
            }
        } else false
    }
}