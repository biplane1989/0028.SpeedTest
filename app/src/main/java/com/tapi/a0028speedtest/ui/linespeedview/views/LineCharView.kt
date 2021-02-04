package com.tapi.a0028speedtest.ui.viewscustom.linespeedview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs.DataNetwork
import com.tapi.a0028speedtest.util.Constances


class LineCharView : View {

    private val _colorlineDownload: Int = Color.parseColor("#F44336")
    private val _colorlineUpload: Int = Color.GREEN
    private val _density = Constances.DEFAULT_DENSITY_VALUE
    private val _ratioSmooth = Constances.DEFAULT_SMOOTH
    private val _ratioGap = Constances.DEFAULT_GAP
  private  val lineDownload = LineDownloadDrawer(this)
    private val lineUpload = LineUpLoadDrawer(this)


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
        lineDownload.init()
        lineUpload.init()
        initAttributeSet(context, attrs, defStyleAttr)
    }


    private fun initAttributeSet(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null || context == null) return
        val a = context.obtainStyledAttributes(attrs, R.styleable.LineCharView, defStyleAttr, 0)
        ratioGap = a.getInteger(R.styleable.LineCharView_lv_ratiogap, Constances.DEFAULT_GAP)

        ratioSmooth =
            a.getInteger(R.styleable.LineCharView_lv_ratioSmoothLine, Constances.DEFAULT_SMOOTH)

        density =
            a.getInteger(R.styleable.LineCharView_lv_densityscreen, Constances.DEFAULT_DENSITY_VALUE)
        colorlineDownload =
            a.getColor(R.styleable.LineCharView_lv_color_line_download, Color.parseColor("#F44336"))
        colorlineUpload =
            a.getColor(R.styleable.LineCharView_lv_color_line_upload, Color.GREEN)

        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        lineDownload.onDraw(canvas)
        lineUpload.onDraw(canvas)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lineDownload.onSizeChange()
        lineUpload.onSizeChange()
    }

    fun resetLineChar(){
        lineDownload.resetLineChar()
        lineUpload.resetLineChar()
    }

    fun startDrawDownload(data: DataNetwork) {
        lineDownload.startDrawLine(data)
    }

    fun startDrawUpload(data: DataNetwork) {
        lineUpload.startDrawLine(data)
    }

    private fun setLineGap(ratio: Int) {
        lineDownload.setGap(ratio)
        lineUpload.setGap(ratio)
    }

    private fun setRatioSmoothLine(ratio: Int) {
        lineDownload.setPrSmooth(ratio)
        lineUpload.setPrSmooth(ratio)
    }

    private fun setDensityScreen(ratio: Int) {
        lineDownload.setDensityScreen(ratio)
        lineUpload.setDensityScreen(ratio)
    }

     fun setColorLineDownload(value: Int) {
        lineDownload.setColorLineDownload(value)
    }

     fun setColorLineUpload(value: Int) {
        lineUpload.setColorLineUpload(value)
    }


}