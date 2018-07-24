package com.lodz.android.componentkt.widget.rv.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * 左侧进入
 * Created by zhouL on 2018/6/28.
 */
class SlideInLeftAnimation(private val duration: Int = 300) : BaseAnimation {

    override fun getAnimators(view: View) = arrayOf<Animator>(ObjectAnimator.ofFloat(view, "translationX", -view.rootView.width.toFloat(), 0f))

    override fun getDuration() = duration
}