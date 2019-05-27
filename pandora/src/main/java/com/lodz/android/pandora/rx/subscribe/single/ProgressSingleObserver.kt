package com.lodz.android.pandora.rx.subscribe.single

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.R
import io.reactivex.disposables.Disposable

/**
 * 展示加载框的订阅者
 * Created by zhouL on 2019/1/18.
 */
abstract class ProgressSingleObserver<T> : RxSingleObserver<T>() {

    final override fun onRxSubscribe(d: Disposable) {
        super.onRxSubscribe(d)
        showProgress()
        onPgSubscribe(d)
    }

    final override fun onRxSuccess(any: T) {
        onPgSuccess(any)
        dismissProgress()
    }

    final override fun onRxError(e: Throwable, isNetwork: Boolean) {
        onPgError(e, isNetwork)
        dismissProgress()
    }

    /** 加载框 */
    private var mProgressDialog: AlertDialog? = null

    /** 创建自定义加载框[dialog] */
    fun create(dialog: AlertDialog): ProgressSingleObserver<T> {
        try {
            mProgressDialog = dialog
            mProgressDialog?.setOnCancelListener {
                cancelDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    /** 创建加载框，配置提示文字资源[strResId]和取消参数[cancelable] */
    fun create(context: Context, @StringRes strResId: Int, cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false): ProgressSingleObserver<T> =
            create(context, context.getString(strResId), cancelable, canceledOnTouchOutside)

    /** 创建加载框，配置提示文字[msg]和返回键关闭[cancelable]默认true，点击空白关闭[canceledOnTouchOutside]默认false */
    fun create(context: Context, msg: String, cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false): ProgressSingleObserver<T> {
        try {
            mProgressDialog = getProgressDialog(context, msg, cancelable, canceledOnTouchOutside)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    /** 获取一个加载框 */
    @SuppressLint("InflateParams")
    private fun getProgressDialog(context: Context, msg: String, cancelable: Boolean, canceledOnTouchOutside: Boolean): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.pandora_view_progress, null)
        val progressDialog = AlertDialog.Builder(context, R.style.ProgressStyle)
                .setView(view)
                .create()
        if (!msg.isEmpty()) {
            val msgTv = view.findViewById<TextView>(R.id.msg)
            msgTv.visibility = View.VISIBLE
            msgTv.text = msg
        }
        progressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        progressDialog.setCancelable(cancelable)
        progressDialog.setOnCancelListener {
            cancelDialog()
        }
        val wd = progressDialog.window
        if (wd != null) {
            wd.setGravity(Gravity.CENTER)
        }
        return progressDialog
    }

    /** 取消加载框 */
    private fun cancelDialog() {// 用户关闭
        dispose()
        onPgCancel()
        dismissProgress()
    }

    /** 显示加载框 */
    private fun showProgress() {
        UiHandler.post {
            try {
                mProgressDialog?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /** 关闭加载框 */
    private fun dismissProgress() {
        UiHandler.post {
            try {
                mProgressDialog?.dismiss()
                mProgressDialog = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onErrorEnd() {// 抛异常关闭
        super.onErrorEnd()
        dismissProgress()
    }

    override fun onDispose() {// 开发者取消
        super.onDispose()
        dismissProgress()
    }

    open fun onPgSubscribe(d: Disposable) {}

    abstract fun onPgSuccess(any: T)

    abstract fun onPgError(e: Throwable, isNetwork: Boolean)

    /** 用户取消回调 */
    open fun onPgCancel() {}

    companion object {
        /** 创建lambda调用 */
        @JvmStatic
        fun <T> action(next: (any: T) -> Unit, error: (e: Throwable, isNetwork: Boolean) -> Unit, context: Context, msg: String = "",
                       cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false): ProgressSingleObserver<T> = object : ProgressSingleObserver<T>() {
            override fun onPgSuccess(any: T) {
                next(any)
            }

            override fun onPgError(e: Throwable, isNetwork: Boolean) {
                error(e, isNetwork)
            }
        }.create(context, msg, cancelable, canceledOnTouchOutside)
    }
}