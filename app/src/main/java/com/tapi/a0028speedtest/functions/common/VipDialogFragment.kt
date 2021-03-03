package com.tapi.a0028speedtest.functions.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseDialog
import com.tapi.a0028speedtest.buttongradient.Rainbow
import com.tapi.a0028speedtest.buttongradient.contextColor
import com.tapi.a0028speedtest.databinding.CommonVipDialogLayoutBinding
import com.tapi.a0028speedtest.util.Constances

enum class UpgradeCode {
    WEEK, MONTH, YEAR
}

class VipDialogFragment : BaseDialog(), View.OnClickListener {
    private var _binding: CommonVipDialogLayoutBinding? = null
    private val binding get() = _binding!!
    private var upgradeCode: UpgradeCode = UpgradeCode.YEAR

    companion object {
        val TAG = "VipDialogFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DefaultDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CommonVipDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        lifecycleScope.launchWhenResumed {
            Rainbow(binding.cardMonthHolder).palette {
                +contextColor(R.color.colorYellowStart)
                +contextColor(R.color.colorYellowCenter1)
                +contextColor(R.color.colorYellowCenter2)
                +contextColor(R.color.colorYellowCenter3)
                +contextColor(R.color.colorYellowCenter4)
                +contextColor(R.color.colorYellowCenter5)
                +contextColor(R.color.colorYellowEnd)
            }.background(radius = 6)
            Rainbow(binding.cardWeekHolder).palette {
                +contextColor(R.color.colorYellowStart)
                +contextColor(R.color.colorYellowCenter1)
                +contextColor(R.color.colorYellowCenter2)
                +contextColor(R.color.colorYellowCenter3)
                +contextColor(R.color.colorYellowCenter4)
                +contextColor(R.color.colorYellowCenter5)
                +contextColor(R.color.colorYellowEnd)
            }.background(radius = 6)
            Rainbow(binding.cardYearHolder).palette {
                +contextColor(R.color.colorYellowStart)
                +contextColor(R.color.colorYellowCenter1)
                +contextColor(R.color.colorYellowCenter2)
                +contextColor(R.color.colorYellowCenter3)
                +contextColor(R.color.colorYellowCenter4)
                +contextColor(R.color.colorYellowCenter5)
                +contextColor(R.color.colorYellowEnd)
            }.background(radius = 6)

//            Rainbow(binding.cardUpgateNow).palette {
//                +contextColor(R.color.colorYellowStart)
//                +contextColor(R.color.colorYellowCenter1)
//                +contextColor(R.color.colorYellowCenter2)
//                +contextColor(R.color.colorYellowCenter3)
//                +contextColor(R.color.colorYellowCenter4)
//                +contextColor(R.color.colorYellowCenter5)
//                +contextColor(R.color.colorYellowEnd)
//            }.background(radius = 8)

//            Rainbow(binding.cardUpgateNow).palette {
//                +contextColor(R.color.colorYellowCenter3)
//                +contextColor(R.color.colorYellowCenter2)
//                +contextColor(R.color.colorYellowCenter1)
//            }.background(radius = 3)

            binding.cardUpgateNow.setOnClickListener(this@VipDialogFragment)
//            binding.cardUpgateNow.enableScale(true)
            binding.cardWeek.setOnClickListener(this@VipDialogFragment)
            binding.cardYear.setOnClickListener(this@VipDialogFragment)
            binding.cardMonth.setOnClickListener(this@VipDialogFragment)
            binding.back.setOnClickListener(this@VipDialogFragment)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.cardWeek -> {
                selectedCard(binding.cardWeek)
            }
            binding.cardYear -> {
                selectedCard(binding.cardYear)
            }
            binding.cardMonth -> {
                selectedCard(binding.cardMonth)
            }
            binding.cardUpgateNow -> {
                sendAction(Constances.ACTION_UPGRADE_VIP)
                dismiss()
            }
            binding.back -> {
                dismiss()
            }
        }
    }

    private fun selectedCard(card: View) {
        when (card) {
            binding.cardWeek -> {
                upgradeCode = UpgradeCode.WEEK
                binding.weekSelectImg.setImageResource(R.drawable.common_check_vip_icon)
                binding.monthSelectImg.setImageResource(R.drawable.common_uncheck_vip_icon)
                binding.yearSelectImg.setImageResource(R.drawable.common_uncheck_vip_icon)
            }
            binding.cardYear -> {
                upgradeCode = UpgradeCode.YEAR
                binding.weekSelectImg.setImageResource(R.drawable.common_uncheck_vip_icon)
                binding.monthSelectImg.setImageResource(R.drawable.common_uncheck_vip_icon)
                binding.yearSelectImg.setImageResource(R.drawable.common_check_vip_icon)
            }
            binding.cardMonth -> {
                upgradeCode = UpgradeCode.MONTH
                binding.weekSelectImg.setImageResource(R.drawable.common_uncheck_vip_icon)
                binding.monthSelectImg.setImageResource(R.drawable.common_check_vip_icon)
                binding.yearSelectImg.setImageResource(R.drawable.common_uncheck_vip_icon)
            }
        }
    }
}