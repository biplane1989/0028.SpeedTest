package com.tapi.a0028speedtest.functions.home.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.tapi.a0028speedtest.base.BaseFragment
import com.tapi.a0028speedtest.databinding.HomeScreenBinding
import com.tapi.a0028speedtest.functions.home.adapter.HomeViewPagerAdapter
import com.tapi.a0028speedtest.functions.home.viewmodel.HomeViewModel
import com.tapi.a0028speedtest.util.Constances


class HomeScreen : BaseFragment(), View.OnClickListener {
    val TAG = "TAG"
    private var _binding: HomeScreenBinding? = null
    private val binding get() = _binding!!

    private val homeViewmodel: HomeViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = HomeScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewmodel = homeViewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        setupView()

    }

    private fun setupView() {
        binding.viewPager.adapter = HomeViewPagerAdapter(childFragmentManager)
        binding.speedtest.setOnClickListener(this)
        binding.history.setOnClickListener(this)
        binding.setting.setOnClickListener(this)


        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        homeViewmodel.clickedSpeedtestImageView()
                    }
                    1 -> {
                        homeViewmodel.clickedHistoryImageView()
                    }
                    2 -> {
                        homeViewmodel.clickedSettingImageView()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    override fun onActionReceived(actionName: String, data: Any?): Boolean {
        if (actionName == Constances.ACTION_TEST_REPEAT){
            binding.viewPager.setCurrentItem(0)
        }
        return super.onActionReceived(actionName, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeScreen().apply {}
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.speedtest -> {
                binding.viewPager.setCurrentItem(0, true)
            }
            binding.history -> {
                binding.viewPager.setCurrentItem(1, true)
            }
            binding.setting -> {
                binding.viewPager.setCurrentItem(2, true)
            }
        }
    }
}