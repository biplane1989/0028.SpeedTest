package com.tapi.a0028speedtest.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("goneIf")
fun View.goneIf(gone: Boolean) {
    visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("visibleIf")
fun View.visibleIf(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}