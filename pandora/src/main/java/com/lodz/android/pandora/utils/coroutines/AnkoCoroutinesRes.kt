package com.lodz.android.pandora.utils.coroutines

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.runOnMainCatch
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.exception.NetworkException
import com.lodz.android.pandora.rx.exception.RxExceptionFactory
import com.lodz.android.pandora.rx.status.ResponseStatus
import com.lodz.android.pandora.utils.progress.ProgressDialogHelper
import kotlinx.coroutines.*

/**
 * 协程接口扩展类
 * @author zhouL
 * @date 2019/11/27
 */

/** 网络接口使用的协程挂起方法，主要对接口进行判断处理 */
fun <T> GlobalScope.runOnSuspendIOCatchRes(
    response: suspend () -> Deferred<T>,
    actionIO: (t: T) -> Unit,
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> }
): Job = launch(Dispatchers.IO) {
    try {
        val res = response.invoke().await()
        if (res is ResponseStatus) {
            if (res.isSuccess()) {
                runOnMain { actionIO(res) }
                return@launch
            }
            val exception = DataException("response fail")
            exception.setData(res)
            throw exception
        }
        runOnMain { actionIO(res) }
    } catch (e: Exception) {
        e.printStackTrace()
        printTagLog(e)
        if (e !is CancellationException) {
            val t = RxExceptionFactory.create(e)
            runOnMain { error(t, t is NetworkException) }
        }
    }
}

/** 网络接口使用的协程方法，主要对接口进行判断处理 */
fun <T> GlobalScope.runOnIOCatchRes(
    response: Deferred<T>,
    actionIO: (t: T) -> Unit,
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> }
): Job = launch(Dispatchers.IO) {
    try {
        val res = response.await()
        if (res is ResponseStatus) {
            if (res.isSuccess()) {
                runOnMain { actionIO(res) }
                return@launch
            }
            val exception = DataException("response fail")
            exception.setData(res)
            throw exception
        }
        runOnMain { actionIO(res) }
    } catch (e: Exception) {
        e.printStackTrace()
        printTagLog(e)
        if (e !is CancellationException) {
            val t = RxExceptionFactory.create(e)
            runOnMain { error(t, t is NetworkException) }
        }
    }
}

/** 带加载框的协程挂起方法 */
fun <T> GlobalScope.runOnSuspendIOCatchPg(
    progressDialog: AlertDialog,
    response: suspend () -> Deferred<T>,
    actionIO: (t: T) -> Unit,
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    runOnMainCatch({ progressDialog.show() })

    val job = launch(Dispatchers.IO) {
        try {
            val res = response.invoke().await()
            if (res is ResponseStatus) {
                if (res.isSuccess()) {
                    runOnMain { actionIO(res) }
                    return@launch
                }
                val exception = DataException("response fail")
                exception.setData(res)
                throw exception
            }
            runOnMain { actionIO(res) }
        } catch (e: Exception) {
            e.printStackTrace()
            printTagLog(e)
            if (e !is CancellationException) {
                val t = RxExceptionFactory.create(e)
                runOnMain { error(t, t is NetworkException) }
            }
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
        })
    }
}

/** 带加载框的协程方法 */
fun <T> GlobalScope.runOnIOCatchPg(
    progressDialog: AlertDialog,
    response: Deferred<T>,
    actionIO: (t: T) -> Unit,
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    runOnMainCatch({ progressDialog.show() })

    val job = launch(Dispatchers.IO) {
        try {
            val res = response.await()
            if (res is ResponseStatus) {
                if (res.isSuccess()) {
                    runOnMain { actionIO(res) }
                    return@launch
                }
                val exception = DataException("response fail")
                exception.setData(res)
                throw exception
            }
            runOnMain { actionIO(res) }
        } catch (e: Exception) {
            e.printStackTrace()
            printTagLog(e)
            if (e !is CancellationException) {
                val t = RxExceptionFactory.create(e)
                runOnMain { error(t, t is NetworkException) }
            }
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
        })
    }
}

/** 带加载框的协程挂起方法 */
fun <T> GlobalScope.runOnSuspendIOCatchPg(
    context: Context,
    msg: String = "正在加载，请稍候",
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = false,
    response: suspend () -> Deferred<T>,
    actionIO: (t: T) -> Unit,
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = launch(Dispatchers.IO) {
        try {
            val res = response.invoke().await()
            if (res is ResponseStatus) {
                if (res.isSuccess()) {
                    runOnMain { actionIO(res) }
                    return@launch
                }
                val exception = DataException("response fail")
                exception.setData(res)
                throw exception
            }
            runOnMain { actionIO(res) }
        } catch (e: Exception) {
            e.printStackTrace()
            printTagLog(e)
            if (e !is CancellationException) {
                val t = RxExceptionFactory.create(e)
                runOnMain { error(t, t is NetworkException) }
            }
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
        })
    }
}

/** 带加载框的协程方法 */
fun <T> GlobalScope.runOnIOCatchPg(
    context: Context,
    msg: String = "正在加载，请稍候",
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = false,
    response: Deferred<T>,
    actionIO: (t: T) -> Unit,
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = launch(Dispatchers.IO) {
        try {
            val res = response.await()
            if (res is ResponseStatus) {
                if (res.isSuccess()) {
                    runOnMain { actionIO(res) }
                    return@launch
                }
                val exception = DataException("response fail")
                exception.setData(res)
                throw exception
            }
            runOnMain { actionIO(res) }
        } catch (e: Exception) {
            e.printStackTrace()
            printTagLog(e)
            if (e !is CancellationException) {
                val t = RxExceptionFactory.create(e)
                runOnMain { error(t, t is NetworkException) }
            }
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
        })
    }
}

/** 打印标签日志 */
private fun printTagLog(t: Throwable) {
    val app = BaseApplication.get() ?: return
    val tag = app.getMetaData(BaseApplication.ERROR_TAG)
    if (tag != null && tag is String) {
        if (tag.isNotEmpty()) {
            PrintLog.e(tag, t.toString(), t)
        }
    }
}