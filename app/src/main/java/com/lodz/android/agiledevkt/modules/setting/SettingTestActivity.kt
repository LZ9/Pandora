package com.lodz.android.agiledevkt.modules.setting

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.rx.subscribe.observer.BaseObserver
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.corekt.utils.*
import io.reactivex.Observable

/**
 * 设置测试类
 * Created by zhouL on 2018/7/20.
 */
class SettingTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 写入设置权限请求码 */
    private val REQUEST_CODE_WRITE_SETTINGS = 77

    /** 亮度模式单选组 */
    @BindView(R.id.brightness_rg)
    lateinit var mBrightnessRadioGroup: RadioGroup
    /** 系统亮度值 */
    @BindView(R.id.system_brightness_tv)
    lateinit var mSystemBrightnessTv: TextView
    /** 系统亮度值拖拽条 */
    @BindView(R.id.system_brightness_sb)
    lateinit var mSystemBrightnessSeekBar: SeekBar
    /** 窗口亮度值 */
    @BindView(R.id.window_brightness_tv)
    lateinit var mWindowBrightnessTv: TextView
    /** 窗口亮度值拖拽条 */
    @BindView(R.id.window_brightness_sb)
    lateinit var mWindowBrightnessSeekBar: SeekBar

    /** 亮度监听器 */
    private val mBrightnessObserver = BrightnessObserver()

    override fun getLayoutId() = R.layout.activity_setting_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 亮度模式单选组
        mBrightnessRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.automatic_rb -> setAutomaticMode()
                R.id.manual_rb -> setManualMode()
            }
        }

        // 系统亮度值拖拽条
        mSystemBrightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mSystemBrightnessSeekBar.progress = progress
                if (fromUser) {
                    setScreenBrightness(progress)
                    return
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 窗口亮度值拖拽条
        mWindowBrightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                mWindowBrightnessSeekBar.progress = progress
                setWindowBrightness(progress)
                mWindowBrightnessTv.setText(getWindowBrightness().toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    /** 亮度变化监听器 */
    inner class BrightnessObserver : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            Observable.just(getScreenBrightness())
                    .compose(RxUtils.ioToMainObservable())
                    .subscribe(object : BaseObserver<Int>() {
                        override fun onBaseNext(any: Int) {
                            mSystemBrightnessTv.setText(any.toString())
                            mSystemBrightnessSeekBar.progress = any
                        }

                        override fun onBaseError(e: Throwable) {
                        }
                    })
        }
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// android6.0以上版本WRITE_SETTINGS需要通过startActivityForResult获取
            if (!Settings.System.canWrite(getContext())) {// 未获取到权限
                requestWriteSettingsPermission()
            } else {
                initLogic()
            }
        } else {
            initLogic()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Settings.System.canWrite(getContext())) {
                initLogic()
            } else {// 用户未开启权限继续申请
                requestWriteSettingsPermission()
            }
        }
    }

    /** 申请设置写入权限 */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestWriteSettingsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS)
        toastShort(R.string.setting_request_permission_tips)
    }

    /** 初始化逻辑 */
    private fun initLogic() {
        contentResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, mBrightnessObserver)
        mBrightnessRadioGroup.check(if (isScreenBrightnessModeAutomatic()) R.id.automatic_rb else R.id.manual_rb)// 根据当前亮度模式设置按钮选中
        showStatusCompleted()
    }

    /** 设置自动模式 */
    private fun setAutomaticMode() {
        setScreenBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
        mSystemBrightnessSeekBar.isEnabled = false
        mWindowBrightnessSeekBar.isEnabled = false
    }

    private fun setManualMode() {
        setScreenBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)

        mSystemBrightnessSeekBar.isEnabled = true
        val systemValue = getScreenBrightness()
        mSystemBrightnessTv.setText(systemValue.toString())
        mSystemBrightnessSeekBar.progress = systemValue

        mWindowBrightnessSeekBar.isEnabled = true
        val windowValue = getWindowBrightness()
        mWindowBrightnessTv.setText(windowValue.toString())
        mWindowBrightnessSeekBar.progress = windowValue
    }

    override fun finish() {
        contentResolver.unregisterContentObserver(mBrightnessObserver)
        super.finish()
    }
}