package com.tapi.a0028speedtest.functions.common

import android.animation.ValueAnimator
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.buttongradient.Rainbow
import com.tapi.a0028speedtest.buttongradient.contextColor
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.databinding.CommonthankDialogLayoutBinding


class ThankDialog(val listener: ThankDialogListener) : DialogFragment() {

    private var _binding: CommonthankDialogLayoutBinding? = null
    private val binding get() = _binding!!
    private var colorAnimation: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DefaultDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommonthankDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        lifecycleScope.launchWhenResumed {
            val mAnimationHeart = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_anim)
            val heartInterpolator = BounceInterpolatorClass(0.2, 20.0)
            mAnimationHeart.interpolator = heartInterpolator

            binding.loveImg.startAnimation(mAnimationHeart)


            binding.cardRate.setOnClickListener {
                listener.actionYes()
                dismiss()
            }

            binding.cardRate.setOnTouchListener { v, event ->
                when(event.action){
                    MotionEvent.ACTION_UP -> {
                        binding.viewRateTxt.alpha = 1f
                    }
                    MotionEvent.ACTION_DOWN -> {
                        binding.viewRateTxt.alpha = 0.5f
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        binding.viewRateTxt.alpha = 1f
                    }
                }
                false
            }
            binding.noThanksTxt.setOnClickListener {
                listener.actionNo()
                dismiss()
            }

             binding.noThanksTxt.setOnTouchListener { v, event ->
                when(event.action){
                    MotionEvent.ACTION_UP -> {
                        binding.noThanksTxt.alpha = 1f
                    }
                    MotionEvent.ACTION_DOWN -> {
                        binding.noThanksTxt.alpha =  0.5f
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        binding.noThanksTxt.alpha = 1f
                    }
                }
                false
            }

            Rainbow(binding.cardRate).palette {
                +contextColor(R.color.colorYellowStart)
                +contextColor(R.color.colorYellowCenter1)
                +contextColor(R.color.colorYellowCenter2)
                +contextColor(R.color.colorYellowCenter3)
                +contextColor(R.color.colorYellowCenter4)
                +contextColor(R.color.colorYellowCenter5)
                +contextColor(R.color.colorYellowEnd)
            }.background(radius = 8)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        colorAnimation?.cancel()
    }

    interface ThankDialogListener {
        fun actionYes()
        fun actionNo()
    }
}