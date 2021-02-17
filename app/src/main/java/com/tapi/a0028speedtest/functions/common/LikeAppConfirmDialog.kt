package com.tapi.a0028speedtest.functions.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseDialog
import com.tapi.a0028speedtest.databinding.CommonLikeAppConfirmDialogBinding

class LikeAppConfirmDialog(val listener: LikeDialogListener) : BaseDialog() {
    private var isLike: Boolean = false
    private var _binding: CommonLikeAppConfirmDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DefaultDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CommonLikeAppConfirmDialogBinding.inflate(inflater, container, false)
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

            binding.smileImg.startAnimation(mAnimationHeart)

            val mAnimationButton = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_anim_button)
            mAnimationButton.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (isLike) {
                        listener.actionLike()
                        dismiss()
                    } else {
                        listener.actionNotLike()
                        dismiss()
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
            binding.likeBtn.setOnClickListener {
                isLike = true
                it.startAnimation(mAnimationButton)
            }
            binding.unlikeBtn.setOnClickListener {
                isLike = false
                it.startAnimation(mAnimationButton)
            }
        }
    }

    interface LikeDialogListener {
        fun actionLike()
        fun actionNotLike()
    }
}