package com.tapi.a0028speedtest.textgradientanimation

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.tapi.a0028speedtest.R


class TextViewGradient(private val mContext: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(
    mContext, attrs
){
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val rainbow: IntArray = getRainbowColors()
        setTextColor(rainbow[0])
        val shader: Shader = LinearGradient(
            0f, 0f, 0f, w.toFloat(), rainbow,
            null, Shader.TileMode.MIRROR
        )

        val matrix = Matrix()
        matrix.setRotate(90f)
        shader.setLocalMatrix(matrix)

        paint.shader = shader
    }

    private fun getRainbowColors(): IntArray {
        return intArrayOf(
         ContextCompat.getColor(context, R.color.colorYellowStart),
         ContextCompat.getColor(context,R.color.colorYellowCenter1),
         ContextCompat.getColor(context,R.color.colorYellowCenter2),
         ContextCompat.getColor(context,R.color.colorYellowCenter3),
         ContextCompat.getColor(context,R.color.colorYellowCenter4),
         ContextCompat.getColor(context,R.color.colorYellowCenter5),
         ContextCompat.getColor(context,R.color.colorYellowEnd)
        )
    }

}