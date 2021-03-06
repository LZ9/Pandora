package com.lodz.android.pandora.widget.rv.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.sp2px

/**
 * 分组标签基类
 * Created by zhouL on 2018/11/22.
 */
open class BaseSectionItemDecoration(context: Context) : BaseItemDecoration(context) {

    companion object {
        /** 默认文字大小 */
        protected const val DEFAULT_TEXT_SIZE_SP = 20f
        /** 默认分组高度 */
        protected const val DEFAULT_SECTION_HEIGHT_DP = 32
    }

    /** 文字画笔 */
    private val mPdrTextPaint: TextPaint
    /** 背景画笔 */
    private val mPdrBgPaint: Paint = Paint()
    /** 分组高度 */
    protected var mPdrSectionHeightPx = 0
    /** 文字左侧间距 */
    private var mPdrTextPaddingLeftDp = 0

    init {
        mPdrBgPaint.color = Color.GRAY

        mPdrTextPaint = TextPaint()
        mPdrTextPaint.color = Color.BLACK
        mPdrTextPaint.typeface = Typeface.DEFAULT_BOLD
        mPdrTextPaint.isAntiAlias = true
        mPdrTextPaint.isDither = true
        mPdrTextPaint.textSize = getContext().sp2px(DEFAULT_TEXT_SIZE_SP)
        mPdrTextPaint.textAlign = Paint.Align.LEFT

        mPdrSectionHeightPx = getContext().dp2px(DEFAULT_SECTION_HEIGHT_DP)
    }

    /** 设置分组的高度[height]（单位dp） */
    fun setSectionHeight(height: Int): BaseSectionItemDecoration {
        mPdrSectionHeightPx = getContext().dp2px(height)
        return this
    }

    /** 设置文字左侧间距[paddingLeft]（单位dp） */
    fun setSectionTextPaddingLeftDp(paddingLeft: Int): BaseSectionItemDecoration {
        mPdrTextPaddingLeftDp = getContext().dp2px(paddingLeft)
        return this
    }

    /** 设置文字的大小[textSize]（单位sp） */
    fun setSectionTextSize(@FloatRange(from = 1.0) textSize: Float): BaseSectionItemDecoration {
        mPdrTextPaint.textSize = getContext().sp2px(if (textSize <= 0) DEFAULT_TEXT_SIZE_SP else textSize)
        return this
    }

    /** 设置文字的字体样式[typeface]（例如：Typeface.DEFAULT、Typeface.DEFAULT_BOLD等等） */
    fun setSectionTextTypeface(typeface: Typeface): BaseSectionItemDecoration {
        mPdrTextPaint.typeface = typeface
        return this
    }

    /** 设置分组文字颜色[color] */
    fun setSectionTextColorRes(@ColorRes color: Int): BaseSectionItemDecoration {
        mPdrTextPaint.color = getContext().getColorCompat(color)
        return this
    }

    /** 设置分组文字颜色[color] */
    fun setSectionTextColorInt(@ColorInt color: Int): BaseSectionItemDecoration {
        mPdrTextPaint.color = color
        return this
    }

    /** 设置分组背景颜色[color] */
    fun setSectionBgColorRes(@ColorRes color: Int): BaseSectionItemDecoration {
        mPdrBgPaint.color = getContext().getColorCompat(color)
        return this
    }

    /** 设置分组背景颜色[color] */
    fun setSectionBgColorInt(@ColorInt color: Int): BaseSectionItemDecoration {
        mPdrBgPaint.color = color
        return this
    }

    /** 画背景，画布[canvas]，左侧坐标[left]，顶部坐标[top]，右侧坐标[right]，底部坐标[bottom] */
    protected fun drawBgPaint(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        canvas.drawRect(checkValue(left).toFloat(), checkValue(top).toFloat(), checkValue(right).toFloat(), checkValue(bottom).toFloat(), mPdrBgPaint)//绘制背景色
    }

    /** 画文字，画布[canvas]，文字[text]，左侧坐标[left]，顶部坐标[top]，右侧坐标[right]，底部坐标[bottom] */
    protected open fun drawTextPaint(canvas: Canvas, text: String, left: Int, top: Int, right: Int, bottom: Int) {
        val fontMetrics = mPdrTextPaint.fontMetrics
        val baseline = (bottom + top - fontMetrics.bottom - fontMetrics.top) / 2//文字居中基线
        canvas.drawText(text, checkValue((left + mPdrTextPaddingLeftDp).toFloat()), checkValue(baseline), mPdrTextPaint)//绘制文本
    }

    /** 是否是垂直列表 */
    protected fun isVerLinearLayout(parent: RecyclerView): Boolean {
        val layoutManager = parent.layoutManager ?: return false
        if (layoutManager is GridLayoutManager) {//GridLayoutManager继承LinearLayoutManager需要先判断
            return false
        }
        if (layoutManager is LinearLayoutManager) {
            return layoutManager.orientation == RecyclerView.VERTICAL
        }
        return false
    }
}