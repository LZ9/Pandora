package com.lodz.android.pandora.mvvm.vm

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lodz.android.pandora.base.application.BaseApplication

/**
 * 基础ViewModel
 * @author zhouL
 * @date 2019/11/29
 */
open class AbsViewModel : ViewModel() {

    var mPdrLongToastMsg = MutableLiveData<String>()
    var mPdrShortToastMsg = MutableLiveData<String>()
    var isPdrFinish = MutableLiveData<Boolean>()

    /** 显示短提示，[msg]消息 */
    protected fun toastShort(msg: String) { mPdrShortToastMsg.value = msg }

    /** 显示长提示，[msg]消息 */
    protected fun toastLong(msg: String) { mPdrLongToastMsg.value = msg }

    /** 显示短提示，[id]文字资源id */
    protected fun toastShort(@StringRes id: Int) { mPdrShortToastMsg.value = BaseApplication.get()?.getString(id) ?: "" }

    /** 显示长提示，[id]文字资源id */
    protected fun toastLong(@StringRes id: Int) { mPdrLongToastMsg.value = BaseApplication.get()?.getString(id) ?: "" }

    /** 关闭Activity */
    protected fun finish() { isPdrFinish.value = true }
}