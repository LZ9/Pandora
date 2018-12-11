package com.lodz.android.componentkt.widget.rv.decoration

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

/**
 * RV基础装饰器
 * Created by zhouL on 2018/11/22.
 */
open class BaseItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    /** 上下文 */
    private val mContext: Context

    init {
        mContext = context
    }

    fun getContext(): Context = mContext

    /** 校验数值[value] */
    protected fun checkValue(value: Int): Int = if (value <= 0) 0 else value

    /** 校验数值[value] */
    protected fun checkValue(value: Float): Float = if (value <= 0f) 0f else value

}