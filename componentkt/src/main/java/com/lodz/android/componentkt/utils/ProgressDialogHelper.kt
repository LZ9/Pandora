package com.lodz.android.componentkt.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.lodz.android.componentkt.R

/**
 * 加载框帮助类
 * Created by zhouL on 2018/7/6.
 */
object ProgressDialogHelper {

    /** 获取一个加载框，配置返回取消[cancelable]、点击空白处取消[canceledOnTouchOutside] */
    fun getProgressDialog(context: Context, cancelable: Boolean, canceledOnTouchOutside: Boolean) = getProgressDialog(context, "", cancelable, canceledOnTouchOutside, null)

    /** 获取一个加载框，配置提示文字[msg]、返回取消[cancelable]、点击空白处取消[canceledOnTouchOutside] */
    fun getProgressDialog(context: Context, msg: String, cancelable: Boolean, canceledOnTouchOutside: Boolean) = getProgressDialog(context, msg, cancelable, canceledOnTouchOutside, null)

    /** 获取一个加载框，配置返回取消[cancelable]、点击空白处取消[canceledOnTouchOutside]、取消监听器[listener] */
    fun getProgressDialog(context: Context, cancelable: Boolean, canceledOnTouchOutside: Boolean, listener: DialogInterface.OnCancelListener?) = getProgressDialog(context, "", cancelable, canceledOnTouchOutside, listener)

    /** 获取一个加载框，配置提示文字[msg]、返回取消[cancelable]、点击空白处取消[canceledOnTouchOutside]、取消监听器[listener] */
    @SuppressLint("InflateParams")
    fun getProgressDialog(context: Context, msg: String, cancelable: Boolean, canceledOnTouchOutside: Boolean, listener: DialogInterface.OnCancelListener?): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.componentkt_view_progress_layout, null)
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
        if (listener != null) {
            progressDialog.setOnCancelListener(listener)
        }
        return progressDialog
    }
}