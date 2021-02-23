package com.tapi.a0028speedtest.ui.viewscustom.linespeedview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.functions.main.objs.Constance
import com.tapi.nettraffic.NetworkRate


class LineCharView : View {

    private val _colorlineDownload: Int = Color.parseColor("#F44336")
    private val _colorlineUpload: Int = Color.GREEN
    private val _density = Constance.DEFAULT_DENSITY_VALUE
    private val _ratioSmooth = Constance.DEFAULT_SMOOTH
    private val _ratioGap = Constance.DEFAULT_GAP
    private val lineDrawer = LineDrawer(this)
//    private val lineUpload = LineUpLoadDrawer(this)


    private var ratioGap: Int
        get() = _ratioGap
        set(value) = setLineGap(value)


    private var ratioSmooth: Int
        get() = _ratioSmooth
        set(value) = setRatioSmoothLine(value)


    private var density: Int
        get() = _density
        set(value) = setDensityScreen(value)


    private var colorlineDownload: Int
        get() = _colorlineDownload
        set(value) = setColorLineDownload(value)


    private var colorlineUpload: Int
        get() = _colorlineUpload
        set(value) = setColorLineUpload(value)


    constructor(context: Context?) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }


    fun init(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        lineDrawer.init()
//        lineUpload.init()
        initAttributeSet(context, attrs, defStyleAttr)
    }


    private fun initAttributeSet(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null || context == null) return
        val a = context.obtainStyledAttributes(attrs, R.styleable.LineCharView, defStyleAttr, 0)
        ratioGap = a.getInteger(R.styleable.LineCharView_lv_ratiogap, Constance.DEFAULT_GAP)

        ratioSmooth =
            a.getInteger(R.styleable.LineCharView_lv_ratioSmoothLine, Constance.DEFAULT_SMOOTH)

        density =
            a.getInteger(R.styleable.LineCharView_lv_densityscreen, Constance.DEFAULT_DENSITY_VALUE)
        colorlineDownload =
            a.getColor(R.styleable.LineCharView_lv_color_line_download, Color.parseColor("#F44336"))
        colorlineUpload =
            a.getColor(R.styleable.LineCharView_lv_color_line_upload, Color.GREEN)

        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        lineDrawer.onDraw(canvas)
//        lineUpload.onDraw(canvas)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != 0 && height != 0) {
            Log.d("TAG", "onSizeChanged: width $w height $h  ")
            lineDrawer.onSizeChange()
//            lineUpload.onSizeChange()
        }
    }

    fun resetLineChar(typeReset: TypeLineView) {
        lineDrawer.resetLineChar(typeReset)
    }


    fun startDrawDownload(data: Float, pos: Float) {
        lineDrawer.startDrawLineDownload(data, pos)
    }

    fun startDrawDownload(list: List<NetworkRate>) {
        lineDrawer.startDrawLineDownload(list)
    }


    fun startDrawUpload(data: Float, pos: Float) {
        lineDrawer.startDrawLineUpload(data, pos)
    }

    fun startDrawUpload(list: List<NetworkRate>) {
        lineDrawer.startDrawLineUpload(list)
    }

    fun setLineGap(ratio: Int) {
        lineDrawer.setGap(ratio)
    }

    fun setLineGapUpload(ratio: Int) {
        lineDrawer.setGapUpload(ratio)
    }

    fun setLineGapDownload(ratio: Int) {
        lineDrawer.setGapDownload(ratio)
    }

    private fun setRatioSmoothLine(ratio: Int) {
        lineDrawer.setPrSmooth(ratio)
//        lineUpload.setPrSmooth(ratio)
    }

    /***
     *  [desity] is ratio of line & width
     */

    fun setDensityScreen(density: Int) {
        lineDrawer.setDensityScreen(density)
//        lineUpload.setDensityScreen(density)
    }

    fun getDensityUnit(): Int {
        return lineDrawer.getDensity()
    }

    fun setColorLineDownload(value: Int) {
        lineDrawer.setColorLineDownload(value)
    }

    fun setColorLineUpload(value: Int) {
        lineDrawer.setColorLineUpload(value)
    }


}

enum class TypeLineView {
    DOWNLOAD, UPLOAD, ALL
}
