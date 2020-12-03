package com.lodz.android.agiledevkt.modules.mvp.base.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvp.base.MvpTestBasePresenter
import com.lodz.android.agiledevkt.modules.mvp.base.MvpTestBaseViewContract
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvp.base.fragment.MvpBaseFragment

/**
 * 带基础控件的MVP测试类
 * @author zhouL
 * @date 2020/12/3
 */
class MvpTestBaseFragment : MvpBaseFragment<MvpTestBasePresenter, MvpTestBaseViewContract>(), MvpTestBaseViewContract {

    companion object {
        fun newInstance(): MvpTestBaseFragment = MvpTestBaseFragment()
    }

    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun createMainPresenter(): MvpTestBasePresenter = MvpTestBasePresenter()

    override fun getLayoutId(): Int = R.layout.activity_mvp_test

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvp_demo_base_title)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getPresenterContract()?.getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)

        mGetSuccessResultBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(true)
        }
        mGetFailResultBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(false)
        }
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusLoading()
        getPresenterContract()?.getResult(true)
    }

    override fun showResult() {
        mResultTv.visibility = View.VISIBLE
    }

    override fun setResult(result: String) {
        mResultTv.text = result
    }
}