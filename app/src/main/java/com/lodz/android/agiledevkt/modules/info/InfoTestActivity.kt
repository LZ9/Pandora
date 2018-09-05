package com.lodz.android.agiledevkt.modules.info

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.rx.subscribe.observer.BaseObserver
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.network.NetInfo
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.corekt.utils.*
import io.reactivex.Observable

/**
 * 信息展示测试类
 * Created by zhouL on 2018/7/27.
 */
class InfoTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, InfoTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 手机UA */
    @BindView(R.id.ua_tv)
    lateinit var mUaTv: TextView

    /** 语言 */
    @BindView(R.id.language_tv)
    lateinit var mLanguageTv: TextView
    /** 国家 */
    @BindView(R.id.country_tv)
    lateinit var mCountryTv: TextView

    /** 品牌 */
    @BindView(R.id.brand_tv)
    lateinit var mBrandTv: TextView
    /** 型号 */
    @BindView(R.id.model_tv)
    lateinit var mModelTv: TextView
    /** 版本 */
    @BindView(R.id.board_tv)
    lateinit var mBoardTv: TextView
    /** CPU1 */
    @BindView(R.id.cpu_abi_tv)
    lateinit var mCpuAbiTv: TextView
    /** CPU2 */
    @BindView(R.id.cpu_abi2_tv)
    lateinit var mCpuAbi2Tv: TextView
    /** 制造商 */
    @BindView(R.id.manufacturer_tv)
    lateinit var mManufacturerTv: TextView
    /** 产品 */
    @BindView(R.id.product_tv)
    lateinit var mProductTv: TextView
    /** 设备 */
    @BindView(R.id.device_tv)
    lateinit var mDeviceTv: TextView

    /** IMEI1 */
    @BindView(R.id.imei1_tv)
    lateinit var mImei1Tv: TextView
    /** IMEI2 */
    @BindView(R.id.imei2_tv)
    lateinit var mImei2Tv: TextView
    /** 双卡双待 */
    @BindView(R.id.dual_sim_tv)
    lateinit var mDualSimTv: TextView
    /** IMSI1 */
    @BindView(R.id.imsi1_tv)
    lateinit var mImsi1Tv: TextView
    /** sim1可用 */
    @BindView(R.id.sim1_ready_tv)
    lateinit var mSim1ReadyTv: TextView
    /** IMSI2 */
    @BindView(R.id.imsi2_tv)
    lateinit var mImsi2Tv: TextView
    /** sim2可用 */
    @BindView(R.id.sim2_ready_tv)
    lateinit var mSim2ReadyTv: TextView
    /** sim卡数据连接状态 */
    @BindView(R.id.sim_data_state_tv)
    lateinit var mSimDataStateTv: TextView
    /** sim卡是否已连接数据 */
    @BindView(R.id.sim_connected_data_tv)
    lateinit var mSimConnectedDataTv: TextView
    /** APN名称 */
    @BindView(R.id.apn_name_tv)
    lateinit var mApnNameTv: TextView

    /** 屏幕高度 */
    @BindView(R.id.screen_height_tv)
    lateinit var mScreenHeightTv: TextView
    /** 屏幕宽度 */
    @BindView(R.id.screen_width_tv)
    lateinit var mScreenWidthTv: TextView
    /** 状态栏高度 */
    @BindView(R.id.status_bar_height_tv)
    lateinit var StatusBarHeightTv: TextView
    /** 是否存在虚拟按键 */
    @BindView(R.id.has_navigation_bar_tv)
    lateinit var mHasNavigationBarTv: TextView
    /** 虚拟按键高度 */
    @BindView(R.id.navigation_bar_height_tv)
    lateinit var mNavigationBarHeightTv: TextView

    /** 是否主线程 */
    @BindView(R.id.main_thread_tv)
    lateinit var mMainThreadTv: TextView
    /** 随机UUID */
    @BindView(R.id.uuid_tv)
    lateinit var mUuidTv: TextView
    /** 应用名称 */
    @BindView(R.id.app_name_tv)
    lateinit var mAppNameTv: TextView
    /** 版本名称 */
    @BindView(R.id.version_name_tv)
    lateinit var mVersionNameTv: TextView
    /** 版本号 */
    @BindView(R.id.version_code_tv)
    lateinit var mVersionCodeTv: TextView
    /** 是否在主进程 */
    @BindView(R.id.main_process_tv)
    lateinit var mMainProcessTv: TextView
    /** 当前进程名称 */
    @BindView(R.id.process_name_tv)
    lateinit var mProcessNameTv: TextView
    /** 是否安装QQ */
    @BindView(R.id.qq_installed_tv)
    lateinit var mQQInstalledTv: TextView
    /** 是否安装微信 */
    @BindView(R.id.wechat_installed_tv)
    lateinit var mWechatInstalledTv: TextView
    /** 安装的应用数量 */
    @BindView(R.id.installed_app_num_tv)
    lateinit var mInstalledAppNumTv: TextView
    /** GPS是否打开 */
    @BindView(R.id.is_gps_open_tv)
    lateinit var mGpsOpenTv: TextView
    /** assets内文本内容 */
    @BindView(R.id.assets_text_tv)
    lateinit var mAssetsTextTv: TextView

    /** 内部存储路径 */
    @BindView(R.id.internal_storage_path_tv)
    lateinit var mInternalStoragePathTv: TextView
    /** 外部存储路径 */
    @BindView(R.id.external_storage_path_tv)
    lateinit var mExternalStoragePathTv: TextView


    /** GPS开关广播接收器*/
    private val mGpsBroadcastReceiver = GpsBroadcastReceiver()

    override fun getLayoutId() = R.layout.activity_info_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showDeviceInfo()
        showScreenInfo()
        showAppInfo()
        showStorage()
        showStatusCompleted()
    }

    /** 显示设备信息 */
    private fun showDeviceInfo() {
        NetworkManager.get().addNetworkListener(mNetworkListener)
        mUaTv.text = getString(R.string.info_phone_ua).format(DeviceUtils.getUserAgent())
        mLanguageTv.text = getString(R.string.info_phone_language).format(DeviceUtils.getLanguage())
        mCountryTv.text = getString(R.string.info_phone_country).format(DeviceUtils.getCountry())

        mBrandTv.text = getString(R.string.info_phone_brand).format(DeviceUtils.getDeviceValue(DeviceUtils.BRAND))
        mModelTv.text = getString(R.string.info_phone_model).format(DeviceUtils.getDeviceValue(DeviceUtils.MODEL))
        mBoardTv.text = getString(R.string.info_phone_board).format(DeviceUtils.getDeviceValue(DeviceUtils.BOARD))
        mCpuAbiTv.text = getString(R.string.info_phone_cpu_abi).format(DeviceUtils.getDeviceValue(DeviceUtils.CPU_ABI))
        mCpuAbi2Tv.text = getString(R.string.info_phone_cpu_abi2).format(DeviceUtils.getDeviceValue(DeviceUtils.CPU_ABI2))
        mManufacturerTv.text = getString(R.string.info_phone_manufacturer).format(DeviceUtils.getDeviceValue(DeviceUtils.MANUFACTURER))
        mProductTv.text = getString(R.string.info_phone_product).format(DeviceUtils.getDeviceValue(DeviceUtils.PRODUCT))
        mDeviceTv.text = getString(R.string.info_phone_device).format(DeviceUtils.getDeviceValue(DeviceUtils.DEVICE))

        mImei1Tv.text = getString(R.string.info_phone_imei1).format(getIMEI1())
        mImei2Tv.text = getString(R.string.info_phone_imei2).format(getIMEI2())
        mDualSimTv.text = getString(R.string.info_is_dual_sim).format(isDualSim().toString())
        mImsi1Tv.text = getString(R.string.info_phone_imsi1).format(getIMSI1())
        mSim1ReadyTv.text = getString(R.string.info_phone_sim1_ready).format(isSim1Ready())
        mImsi2Tv.text = getString(R.string.info_phone_imsi2).format(getIMSI2())
        mSim2ReadyTv.text = getString(R.string.info_phone_sim2_ready).format(isSim2Ready())
        mSimDataStateTv.text = getString(R.string.info_phone_sim_data_state).format(getSimDataState())
        mSimConnectedDataTv.text = getString(R.string.info_phone_is_connected_data).format(isSimDataConnected())
        mApnNameTv.text = getString(R.string.info_apn_name).format(getApnName())
    }

    /** 显示屏幕信息 */
    private fun showScreenInfo() {
        mScreenHeightTv.text = getString(R.string.info_screen_height).format(getScreenHeight())
        mScreenWidthTv.text = getString(R.string.info_screen_width).format(getScreenWidth())

        StatusBarHeightTv.text = getString(R.string.info_status_bar_height).format(getStatusBarHeight())
        mHasNavigationBarTv.text = getString(R.string.info_has_navigation_bar).format(hasNavigationBar())
        mNavigationBarHeightTv.text = getString(R.string.info_navigation_bar_height).format(getNavigationBarHeight())
    }

    /** 显示应用信息 */
    private fun showAppInfo() {
        mMainThreadTv.text = getString(R.string.info_is_main_thread).format(AppUtils.isMainThread())
        mUuidTv.text = getString(R.string.info_uuid).format(AppUtils.getUUID())
        mAppNameTv.text = getString(R.string.info_app_name).format(getAppName())
        mVersionNameTv.text = getString(R.string.info_version_name).format(getVersionName())
        mVersionCodeTv.text = getString(R.string.info_version_code).format(getVersionCode())
        mMainProcessTv.text = getString(R.string.info_is_main_process).format(isMainProcess())
        mProcessNameTv.text = getString(R.string.info_process_name).format(getProcessName())
        mQQInstalledTv.text = getString(R.string.info_is_qq_installed).format(isPkgInstalled("com.tencent.mobileqq"))
        mWechatInstalledTv.text = getString(R.string.info_is_wechat_installed).format(isPkgInstalled("com.tencent.mm"))
        mInstalledAppNumTv.text = getString(R.string.info_installed_app_num).format(getInstalledPackages().size.toString())

        registerGpsReceiver()
        mGpsOpenTv.text = getString(R.string.info_is_gps_open).format(isGpsOpen())

        Observable.just("test.txt")
                .map { fileName ->
                    getAssetsFileContent(fileName)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<String>() {
                    override fun onBaseNext(any: String) {
                        mAssetsTextTv.text = getString(R.string.info_get_assets_text).format(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mAssetsTextTv.text = getString(R.string.info_get_assets_text).format(e.cause)
                    }

                })
    }

    /** 显示存储信息 */
    private fun showStorage() {
        val pathPair = getStoragePath()
        val internal = pathPair.first ?: ""
        val external = pathPair.second ?: ""

        mInternalStoragePathTv.text = getString(R.string.info_internal_storage_path).format(internal)
        mExternalStoragePathTv.text = getString(R.string.info_external_storage_path).format(external)
    }

    /** 注册GPS广播接收器 */
    private fun registerGpsReceiver() {
        try {
            val fileter = IntentFilter()
            fileter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            registerReceiver(mGpsBroadcastReceiver, fileter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun finish() {
        try {
            NetworkManager.get().removeNetworkListener(mNetworkListener)
            unregisterReceiver(mGpsBroadcastReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.finish()
    }

    /** 网络监听器 */
    private val mNetworkListener = object : NetworkManager.NetworkListener {
        override fun onNetworkStatusChanged(isNetworkAvailable: Boolean, netInfo: NetInfo) {
            mSimDataStateTv.text = getString(R.string.info_phone_sim_data_state).format(getSimDataState())
            mSimConnectedDataTv.text = getString(R.string.info_phone_is_connected_data).format(isSimDataConnected())
            mApnNameTv.text = getString(R.string.info_apn_name).format(getApnName())
        }
    }

    /** GPS广播接收器 */
    inner class GpsBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                return
            }
            val action = intent.action
            if (action.isNullOrEmpty()){
                return
            }
            if (action!!.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                mGpsOpenTv.text = getString(R.string.info_is_gps_open).format(isGpsOpen())
            }
        }

    }

}