package com.tapi.a0028speedtest.ui.viewscustom.circlewaveview

import android.animation.ValueAnimator
import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.tapi.a0028speedtest.R
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class CircleWaveView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    private var ringWidth: Float = 0f
    private var cy: Float = 0f
    private var cx: Float = 0f
    private val LINE_SMOOTHNESS = 0.16f
    private val path: Path
    private val path2: Path
    private val paint: Paint
    private val paint2: Paint
    private val paintButton: Paint
    private val textPaint: Paint
    private var pointCount: Int
    private var pointCount2: Int
    private var color1Round1: Int = Color.RED
    private var color2Round1: Int = Color.BLUE
    private var color1Round2: Int = Color.RED
    private var color2Round2: Int = Color.BLUE
    private var innerSizeRatio = 0.75f
    private lateinit var firstRadius: FloatArray
    private lateinit var secondRadius: FloatArray
    private lateinit var animationOffset: FloatArray
    private lateinit var firstRadius2: FloatArray
    private lateinit var secondRadius2: FloatArray
    private lateinit var animationOffset2: FloatArray
    private lateinit var xs: FloatArray
    private lateinit var xs2: FloatArray
    private lateinit var ys: FloatArray
    private lateinit var ys2: FloatArray
    private lateinit var fractions: FloatArray
    private lateinit var fractions2: FloatArray
    private lateinit var animators: Array<ValueAnimator?>
    private lateinit var animators2: Array<ValueAnimator?>
    private var angleOffset = 0f
    private var angleOffset2 = 0f
    private var currentCx = 0f
    private var currentCy = 0f
    private var translationRadius = 0f
    private var translationRadiusStep = 0f
    private var useAnimation: Boolean
    private var lastTranslationAngle = 0f
    private var temp = FloatArray(2)
    private val text = context.getString(R.string.start)

    private fun measureWidthText(text: String): Float {
        return textPaint.measureText(text)
    }

    private fun randomTranslate() {
        val r = translationRadiusStep
        val R = translationRadius
        cx = (width / 2).toFloat()
        cy = (height / 2).toFloat()
        val vx = currentCx - cx
        val vy = currentCy - cy
        val ratio = 1 - r / R
        val wx = vx * ratio
        val wy = vy * ratio
        lastTranslationAngle =
            ((Math.random() - 0.5) * Math.PI / 4 + lastTranslationAngle).toFloat()
        val distRatio = Math.random().toFloat()
        currentCx = (cx + wx + r * distRatio * cos(lastTranslationAngle.toDouble())).toFloat()
        currentCy = (cy + wy + r * distRatio * sin(lastTranslationAngle.toDouble())).toFloat()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    private fun init(pointCount: Int) {
        firstRadius = FloatArray(pointCount)
        secondRadius = FloatArray(pointCount)
        animationOffset = FloatArray(pointCount)
        xs = FloatArray(pointCount)
        ys = FloatArray(pointCount)
        fractions = FloatArray(pointCount + 1)
        animators = arrayOfNulls(pointCount)
        angleOffset = (Math.PI * 2 / pointCount * Math.random()).toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val radius = (min(w, h) / 2).toFloat()
        val innerRadius = radius * innerSizeRatio
        ringWidth = radius - innerRadius
        for (i in 0 until pointCount) {
            firstRadius[i] = (innerRadius + ringWidth * 0.7f)
            secondRadius[i] = (innerRadius + ringWidth * 0.3f)
            animationOffset[i] = Math.random().toFloat()
        }
        for (i in 0 until pointCount2) {
            firstRadius2[i] = (innerRadius + ringWidth * 0.7f)
            secondRadius2[i] = (innerRadius + ringWidth * 0.3f)
            animationOffset2[i] = Math.random().toFloat()
        }
        paint.shader = LinearGradient(
            0f,
            0f,
            0f,
            h.toFloat(),
            color1Round1,
            color2Round1,
            Shader.TileMode.MIRROR
        )
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6f
        currentCx = (w / 2).toFloat()
        currentCy = (h / 2).toFloat()
        translationRadius = radius / 6
        translationRadiusStep = radius / 4000

        paint2.shader = LinearGradient(
            0f,
            0f,
            0f,
            h.toFloat(),
            color1Round2,
            color2Round2,
            Shader.TileMode.MIRROR
        )
        paint2.style = Paint.Style.STROKE
        paint2.strokeWidth = 6f

        setupPaintButton(context)
        setupPaintText(context)
    }

    private fun setupPaintText(context: Context) {
        textPaint.color = ContextCompat.getColor(context, R.color.colorBlueText)
        textPaint.textSize = convertSpToPixels(35f, context)
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        textPaint.flags = Paint.ANTI_ALIAS_FLAG
        textPaint.textAlign = Paint.Align.CENTER
    }

    fun convertSpToPixels(sp: Float, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics
        )
    }

    fun setColorRound1(color1: Int, color2: Int) {
        this.color1Round1 = color1
        this.color2Round1 = color2
        invalidate()
    }

    fun setColorRound2(color1: Int, color2: Int) {
        this.color1Round2 = color1
        this.color2Round2 = color2
        invalidate()
    }

    private fun setupPaintButton(context: Context) {
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
        paintButton.shader = gradient
        paintButton.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until pointCount) {
            val currentFraction: Float =
                if (useAnimation) animators[i]!!.animatedValue as Float else 0f
            val radius = firstRadius[i] * (1 - currentFraction) + secondRadius[i] * currentFraction
            val angle = (Math.PI * 2 / pointCount * i).toFloat() + angleOffset
            xs[i] = (currentCx + radius * cos(angle.toDouble())).toFloat()
            ys[i] = (currentCy + radius * sin(angle.toDouble())).toFloat()
        }
        for (i in 0 until pointCount2) {
            val currentFraction: Float =
                if (useAnimation) animators2[i]!!.animatedValue as Float else 0f
            val radius =
                firstRadius2[i] * (1 - currentFraction) + secondRadius2[i] * currentFraction
            val angle = (Math.PI * 2 / pointCount2 * i).toFloat() + angleOffset2
            xs2[i] = (currentCx + radius * cos(angle.toDouble())).toFloat()
            ys2[i] = (currentCy + radius * sin(angle.toDouble())).toFloat()
        }
        path.reset()
        path.moveTo(xs[0], ys[0])
        for (i in 0 until pointCount) {
            val currX = getFromArray(xs, i)
            val currY = getFromArray(ys, i)
            val nextX = getFromArray(xs, i + 1)
            val nextY = getFromArray(ys, i + 1)
            getVector(xs, ys, i, temp)
            val vx = temp[0]
            val vy = temp[1]
            getVector(xs, ys, i + 1, temp)
            val vxNext = temp[0]
            val vyNext = temp[1]
            path.cubicTo(currX + vx, currY + vy, nextX - vxNext, nextY - vyNext, nextX, nextY)
        }
        canvas.save()
        canvas.drawPath(path, paint)
        path2.reset()
        path2.moveTo(xs2[0], ys2[0])
        for (i in 0 until pointCount2) {
            val currX = getFromArray(xs2, i)
            val currY = getFromArray(ys2, i)
            val nextX = getFromArray(xs2, i + 1)
            val nextY = getFromArray(ys2, i + 1)
            getVector(xs2, ys2, i, temp)
            val vx = temp[0]
            val vy = temp[1]
            getVector(xs2, ys2, i + 1, temp)
            val vxNext = temp[0]
            val vyNext = temp[1]
            path2.cubicTo(currX + vx, currY + vy, nextX - vxNext, nextY - vyNext, nextX, nextY)
        }
        canvas.drawPath(path2, paint2)
        canvas.drawCircle(cx, cy, secondRadius[1] - (ringWidth / 4f), paintButton)
        canvas.drawText(
            text,
            width / 2f,
            (height / 2 - (textPaint.descent() + textPaint.ascent()) / 2),
            textPaint
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (useAnimation) {
            for (animator in animators) {
                animator!!.end()
            }
            for (animator in animators2) {
                animator!!.end()
            }
        }
    }

    private fun getFromArray(arr: FloatArray, pos: Int): Float {
        return arr[(pos + arr.size) % arr.size]
    }

    private fun getVector(xs: FloatArray, ys: FloatArray, i: Int, out: FloatArray) {
        val nextX = getFromArray(xs, i + 1)
        val nextY = getFromArray(ys, i + 1)
        val prevX = getFromArray(xs, i - 1)
        val prevY = getFromArray(ys, i - 1)
        val vx = (nextX - prevX) * LINE_SMOOTHNESS
        val vy = (nextY - prevY) * LINE_SMOOTHNESS
        out[0] = vx
        out[1] = vy
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundWaveView)
        pointCount = typedArray.getInt(R.styleable.RoundWaveView_pointCount, 6)
        pointCount2 = typedArray.getInt(R.styleable.RoundWaveView_pointCount2, 6)
        useAnimation = typedArray.getBoolean(R.styleable.RoundWaveView_useAnimation, true)
        typedArray.recycle()
        init(pointCount)
        init2(pointCount2)
        path = Path()
        path2 = Path()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        paintButton = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val delta = 1.0f / pointCount
        val delta2 = 1.0f / pointCount2
        for (i in fractions.indices) {
            fractions[i] = delta * i
        }
        for (i in fractions2.indices) {
            fractions2[i] = delta2 * i
        }
        for (i in 0 until pointCount) {
            var pos = (Math.random() * pointCount).toInt()
            val dest = FloatArray(pointCount * 2 + 1)
            var inc = 1
            for (j in dest.indices) {
                dest[j] = fractions[pos]
                pos += inc
                if (pos < 0 || pos >= fractions.size) {
                    inc = -inc
                    pos += inc * 2
                }
            }
            if (i == 0) {
                val list: MutableList<Float> = ArrayList()
                for (f in dest) {
                    list.add(f)
                }
                Log.d(ContentValues.TAG, "DEST $list")
            }
            if (useAnimation) {
                val animator =
                    ValueAnimator.ofFloat(*dest).setDuration((1000 + Math.random() * 500).toLong())
                animator.interpolator = LinearInterpolator()
                animator.repeatMode = ValueAnimator.RESTART
                animator.repeatCount = ValueAnimator.INFINITE
                animator.start()
                if (i == 0) {
                    animator.addUpdateListener {
                        randomTranslate()
                        invalidate()
                    }
                }
                animators[i] = animator
                animator.start()
            }
        }
        for (i in 0 until pointCount2) {
            var pos = (Math.random() * pointCount2).toInt()
            val dest = FloatArray(pointCount2 * 2 + 1)
            var inc = 1
            for (j in dest.indices) {
                dest[j] = fractions2[pos]
                pos += inc
                if (pos < 0 || pos >= fractions2.size) {
                    inc = -inc
                    pos += inc * 2
                }
            }
            if (i == 0) {
                val list: MutableList<Float> = ArrayList()
                for (f in dest) {
                    list.add(f)
                }
                Log.d(ContentValues.TAG, "DEST $list")
            }
            if (useAnimation) {
                val animator =
                    ValueAnimator.ofFloat(*dest).setDuration((1000 + Math.random() * 500).toLong())
                animator.interpolator = LinearInterpolator()
                animator.repeatMode = ValueAnimator.RESTART
                animator.repeatCount = ValueAnimator.INFINITE
                animator.start()
                if (i == 0) {
                    animator.addUpdateListener {
                        randomTranslate()
                        invalidate()
                    }
                }
                animators2[i] = animator
                animator.start()
            }
        }
    }

    private fun init2(pointCount2: Int) {
        firstRadius2 = FloatArray(pointCount2)
        secondRadius2 = FloatArray(pointCount2)
        animationOffset2 = FloatArray(pointCount2)
        xs2 = FloatArray(pointCount2)
        ys2 = FloatArray(pointCount2)
        fractions2 = FloatArray(pointCount2 + 1)
        animators2 = arrayOfNulls(pointCount2)

        angleOffset2 = (Math.PI * 2 / pointCount2 * Math.random()).toFloat()
    }
}