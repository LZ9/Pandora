package com.lodz.android.corekt.anko

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import com.lodz.android.corekt.utils.ReflectUtils

/**
 * 设备扩展类
 * Created by zhouL on 2019/2/1.
 */

/** 获取APN名称 */
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.getApnName(): String {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info: NetworkInfo = manager.activeNetworkInfo ?: return ""
    if (ConnectivityManager.TYPE_MOBILE == info.type && info.extraInfo.isNotEmpty()) {
        return info.extraInfo
    }
    return ""
}

/** 获取手机的IMSI1 */
fun Context.getIMSI1(): String = getOperatorBySlot("getSubscriberId", 0)

/** Sim卡1是否可用 */
fun Context.isSim1Ready(): Boolean {
    var type = getOperatorBySlot("getSimState", 0)
    if (type.isEmpty()) {
        type = getOperatorBySlot("getSimStateGemini", 0)
    }
    return type.isNotEmpty() && type.toInt() == TelephonyManager.SIM_STATE_READY
}

/** 获取手机的IMSI2 */
fun Context.getIMSI2(): String = getOperatorBySlot("getSubscriberId", 1)

/** Sim卡2是否可用 */
fun Context.isSim2Ready(): Boolean {
    var type = getOperatorBySlot("getSimState", 1)
    if (type.isEmpty()) {
        type = getOperatorBySlot("getSimStateGemini", 1)
    }
    return type.isNotEmpty() && type.toInt() == TelephonyManager.SIM_STATE_READY
}

/** sim卡数据连接状态，常见返回值包括TelephonyManager.DATA_DISCONNECTED或TelephonyManager.DATA_CONNECTED */
fun Context.getSimDataState(): Int {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.dataState
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}

/** sim卡数据是否已连接 */
fun Context.isSimDataConnected(): Boolean = getSimDataState() == TelephonyManager.DATA_CONNECTED

/** 获取手机的IMEI1 */
fun Context.getIMEI1(): String {
    var imei = getOperatorBySlot("getDeviceId", 0)
    if (imei.isEmpty()) {
        imei = getOperatorBySlot("getDeviceIdGemini", 0)
    }
    return imei
}

/** 获取手机的IMEI2 */
fun Context.getIMEI2(): String {
    var imei = getOperatorBySlot("getDeviceId", 1)
    if (imei.isEmpty()) {
        imei = getOperatorBySlot("getDeviceIdGemini", 1)
    }
    return imei
}

/** 手机是否双卡双待 */
fun Context.isDualSim(): Boolean = getIMEI2().isNotEmpty()

/** 通过方法名[predictedMethodName]获取对应序号[slotId]下的设备信息 */
private fun Context.getOperatorBySlot(predictedMethodName: String, slotId: Int): String {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val value = ReflectUtils.executeFunction(tm::class.java, tm, predictedMethodName, arrayOf(Int::class.java), arrayOf(slotId))
        if (value != null) {
            return value.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 获取手机Android ID */
@SuppressLint("HardwareIds")
fun Context.getAndroidId(): String {
    val id: String? = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
    return id ?: ""
}

/** 获取手机硬件id兼容方法 */
fun Context.getDeviceIdCompat(): String {
    val imei :String? = getIMEI1()
    if (imei != null && imei.isNotEmpty()){
        return imei
    }
    return getAndroidId()
}