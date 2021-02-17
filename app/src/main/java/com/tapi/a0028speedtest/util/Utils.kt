package com.tapi.a0028speedtest.util

import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.net.Uri
import android.util.TypedValue
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.functions.home.objects.Vector
import kotlin.math.pow

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

    fun findYCoordinatis(oldPoint: PointF, newPoint: PointF, xPos: Float): Float {
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

}