package com.tapi.a0028speedtest.util

import android.content.Context
import android.graphics.PointF
import android.net.ConnectivityManager
import android.util.Log
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tapi.a0028speedtest.functions.main.objs.Vector
import com.tapi.a0028speedtest.ui.viewscustom.speedview.components.Section
import com.tapi.speedtest.ui.speedview.view.Gauge
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import kotlin.math.pow
import com.tapi.a0028speedtest.R

typealias OnSpeedChangeListener = (gauge: Gauge, isSpeedUp: Boolean, isByTremble: Boolean) -> Unit
typealias OnSectionChangeListener = (previousSection: Section?, newSection: Section?) -> Unit
typealias OnPrintTickLabelListener = (tickPosition: Int, tick: Float) -> CharSequence?



fun Gauge.doOnSections(action: (section: Section) -> Unit) {
    val sections = ArrayList(this.sections)
    // this will also clear observers.
    this.clearSections()
    sections.forEach { action.invoke(it) }
    this.addSections(sections)
}

fun getRoundAngle(a: Float, d: Float): Float {
    return (a * .5f * 360 / (d  * Math.PI)).toFloat()
}
object Utils {
    fun dpToPx(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
    }


    fun pxToDp(context: Context, px: Int): Int {
        return (px / context.resources.displayMetrics.density).toInt()
    }

    fun spToPx(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
            .toInt()
    }

    fun convertValue(min1: Float, max1: Float, min2: Float, max2: Float, value: Float): Float {
        return ((value - min1) * ((max2 - min2) / (max1 - min1)) + min2)
    }

    fun shareFileAudio(context: Context, text: String, pkgName: String?): Boolean {
        return try {
            val intent = Intent()
            if (pkgName != null) {
                intent.`package` = pkgName
            }
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM, text)
            intent.type = "audio/*"
            context.startActivity(Intent.createChooser(intent, context.resources.getString(R.string.Choose_in_app_inten)))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun <X, Y, Z> map(
        sourceX: LiveData<X>,
        sourceY: LiveData<Y>,
        mapFunc: (x: X?, y: Y?) -> Z
    ): LiveData<Z> {

        var result = MediatorLiveData<Z>()
        result.addSource(sourceX) {
            result.value = mapFunc(it, sourceY.value)
        }
        result.addSource(sourceY) {
            result.value = mapFunc(sourceX.value, it)
        }
        return result


    }

    fun shareMutilpleFile(context: Context, listUri: ArrayList<Uri>, pkgName: String?) {
        val shareIntent = Intent()
        if (pkgName != null) {
            shareIntent.`package` = pkgName
        }
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, listUri)
        shareIntent.type = "audio/*"
        context.startActivity(Intent.createChooser(shareIntent, context.resources.getString(R.string.Choose_in_app_inten)))
    }



}