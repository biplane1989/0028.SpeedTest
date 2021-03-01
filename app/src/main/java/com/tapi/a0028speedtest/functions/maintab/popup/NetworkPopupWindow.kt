package com.tapi.a0028speedtest.functions.maintab.popup

import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.util.Utils


import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView

enum class ItemType {
    CHANGE, FAVORITES
}

class NetworkPopupWindow(val view: View, val onItemSelected: (type: ItemType) -> Unit) : View.OnClickListener {

    private val testTv: TextView
    private val favoriteTv: TextView
    private val popupView: View
    private val popupWindow: PopupWindow

    init {
        popupView = LayoutInflater.from(view.context)
            .inflate(R.layout.network_provider_poupup, null)
        testTv = popupView.findViewById(R.id.test)
        favoriteTv = popupView.findViewById(R.id.favorite)

        popupWindow = PopupWindow(popupView, Utils.dpToPx(view.context, 170f), Utils.dpToPx(view.context, 115f), true)
    }

    fun show() {
        bindEvents()
        popupWindow.showAsDropDown(view, -Utils.dpToPx(view.context, 150f), -Utils.dpToPx(view.context, 40f))
    }

    private fun bindEvents() {
        testTv.setOnClickListener(this)
        favoriteTv.setOnClickListener(this)
    }

    fun dismis() {
        popupWindow.dismiss()
    }

    override fun onClick(view: View) {
        when (view) {
            testTv -> {
                onItemSelected(ItemType.CHANGE)
                popupWindow.dismiss()
            }
            favoriteTv -> {
                onItemSelected(ItemType.FAVORITES)
                popupWindow.dismiss()
            }
        }
    }
}