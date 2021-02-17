package com.tapi.a0028speedtest.functions.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tapi.a0028speedtest.functions.history.screen.HistoryFragment
import com.tapi.a0028speedtest.functions.setting.screens.SettingScreen

class HomeViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> {
            SettingScreen.newInstance()
        }
        1 -> {
            HistoryFragment.newInstance()
        }
        else -> {
            SettingScreen.newInstance()
        }
    }

    override fun getCount(): Int = 3

}