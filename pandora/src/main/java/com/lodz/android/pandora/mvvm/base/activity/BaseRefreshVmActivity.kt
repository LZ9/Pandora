package com.lodz.android.pandora.mvvm.base.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseRefreshActivity
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel

/**
 * ViewModel基类Activity（带基础状态控件和下拉刷新控件）
 * @author zhouL
 * @date 2019/12/5
 */
abstract class BaseRefreshVmActivity<VM : BaseRefreshViewModel> : BaseRefreshActivity() {

    private val mPdrViewModel by lazy { ViewModelProvider(this).get(createViewModel()) }

    fun getViewModel(): VM = mPdrViewModel

    abstract fun createViewModel(): Class<VM>

    override fun setListeners() {
        super.setListeners()

        getViewModel().isPdrFinish.observe(this, Observer { value ->
            if (value) { finish() }
        })

        getViewModel().mPdrShortToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastShort(value)
        })

        getViewModel().mPdrLongToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastLong(value)
        })

        getViewModel().isPdrShowNoData.observe(this, Observer { value ->
            if (value) { showStatusNoData() }
        })

        getViewModel().isPdrShowError.observe(this, Observer { value ->
            if (value.first) { showStatusError(value.second) }
        })

        getViewModel().isPdrShowLoading.observe(this, Observer { value ->
            if (value) { showStatusLoading() }
        })

        getViewModel().isPdrShowCompleted.observe(this, Observer { value ->
            if (value) { showStatusCompleted() }
        })

        getViewModel().isPdrShowTitleBar.observe(this, Observer { value ->
            if (value) { showTitleBar() } else { goneTitleBar() }
        })

        getViewModel().isPdrRefreshEnabled.observe(this, Observer { value ->
            setSwipeRefreshEnabled(value)
        })

        getViewModel().isPdrRefreshFinish.observe(this, Observer { value ->
            if (value){ setSwipeRefreshFinish() }
        })
    }
}