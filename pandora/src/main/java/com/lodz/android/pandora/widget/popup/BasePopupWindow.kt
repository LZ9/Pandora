package com.lodz.android.pandora.widget.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.LayoutRes

/**
 * PopupWindow基类
 * Created by zhouL on 2018/11/23.
 */
abstract class BasePopupWindow(context: Context) {
    /** 上下文 */
    private val mPdrContext: Context = context
    /** PopupWindow */
    private val mPdrPopupWindow: PopupWindow

    init {
        mPdrPopupWindow = createPopupWindow(mPdrContext)
    }

    private fun createPopupWindow(context: Context): PopupWindow {
        val popView = LayoutInflater.from(context).inflate(getLayoutId(), null)
        val popupWindow = PopupWindow(popView, getWidthPx(), getHeightPx(), true)
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = getElevationValue()
        }
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        return popupWindow
    }

    fun create() {
        startCreate()
        findViews(mPdrPopupWindow.contentView)
        setListeners()
        initData()
        endCreate()
    }

    /** 设置宽度，可重写该方法 */
    protected open fun getWidthPx(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    /** 设置高度，可重写该方法 */
    protected open fun getHeightPx(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    /** 设置阴影值，可重写该方法 */
    protected open fun getElevationValue(): Float = 12f

    protected open fun startCreate() {}

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected open fun findViews(view: View) {}

    protected open fun setListeners() {}

    protected open fun initData() {}

    protected open fun endCreate() {}

    protected fun getContext(): Context = mPdrContext

    fun getPopup(): PopupWindow = mPdrPopupWindow

}