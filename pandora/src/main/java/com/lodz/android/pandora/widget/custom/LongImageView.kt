package com.lodz.android.pandora.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.widget.NestedScrollView
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.pandora.R
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.Observable
import java.io.File

/**
 * 长图控件
 * Created by zhouL on 2019/1/30.
 */
class LongImageView : NestedScrollView {

    /** 根布局 */
    private var mRootLayout: LinearLayout? = null
    /** 占位符 */
    private var mPlaceholderImg: ImageView? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        findViews()
        configLayout(attrs)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.LongImageView)
        }

        // 占位符图片
        val placeholder: Drawable? = typedArray?.getDrawable(R.styleable.LongImageView_placeholderDrawable)
        if (placeholder != null) {
            setPlaceholderDrawable(placeholder)
        } else {
            setPlaceholderRes(R.drawable.pandora_ic_launcher)
        }

        // 占位符图片宽度
        setPlaceholderWidth(typedArray?.getDimensionPixelSize(R.styleable.LongImageView_placeholderWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
                ?: ViewGroup.LayoutParams.WRAP_CONTENT)

        // 占位符图片高度
        setPlaceholderHeight(typedArray?.getDimensionPixelSize(R.styleable.LongImageView_placeholderHeight, ViewGroup.LayoutParams.WRAP_CONTENT)
                ?: ViewGroup.LayoutParams.WRAP_CONTENT)

        // 占位符图片类型
        val scaleType: Int = typedArray?.getInt(R.styleable.LongImageView_placeholderScaleType, -1)
                ?: -1
        if (scaleType != -1) {
            setPlaceholderScaleType(getTypeById(scaleType))
        }

        // 是否显示占位符
        val isShowPlaceholder: Boolean = typedArray?.getBoolean(R.styleable.LongImageView_showPlaceholder, true)
                ?: true
        showPlaceholder(isShowPlaceholder)

        if (typedArray != null) {
            typedArray.recycle()
        }
    }

    /** 通过id获取ScaleType类型 */
    private fun getTypeById(type: Int): ImageView.ScaleType = when (type) {
        0 -> ImageView.ScaleType.MATRIX
        1 -> ImageView.ScaleType.FIT_XY
        2 -> ImageView.ScaleType.FIT_START
        3 -> ImageView.ScaleType.FIT_CENTER
        4 -> ImageView.ScaleType.FIT_END
        5 -> ImageView.ScaleType.CENTER
        6 -> ImageView.ScaleType.CENTER_CROP
        7 -> ImageView.ScaleType.CENTER_INSIDE
        else -> ImageView.ScaleType.CENTER
    }

    private fun findViews() {
        LayoutInflater.from(context).inflate(R.layout.pandora_view_long_img, this)
        mRootLayout = findViewById(R.id.root_layout)
        mPlaceholderImg = findViewById(R.id.placeholder_img)
    }

    /** 设置占位符图片[resId] */
    fun setPlaceholderRes(@DrawableRes resId: Int) {
        mPlaceholderImg?.setImageResource(resId)
    }

    /** 设置占位符图片[drawable] */
    fun setPlaceholderDrawable(drawable: Drawable) {
        mPlaceholderImg?.setImageDrawable(drawable)
    }

    /** 设置占位符图片类型[scaleType] */
    fun setPlaceholderScaleType(scaleType: ImageView.ScaleType) {
        mPlaceholderImg?.scaleType = scaleType
    }

    /** 设置占位符宽度[width] */
    fun setPlaceholderWidth(width: Int) {
        val layoutParams = mPlaceholderImg?.layoutParams
        layoutParams?.width = width
    }

    /** 设置占位符高度[height] */
    fun setPlaceholderHeight(height: Int) {
        val layoutParams = mPlaceholderImg?.layoutParams
        layoutParams?.height = height
    }

    /** 显示占位符[isShow] */
    fun showPlaceholder(isShow: Boolean = true) {
        mPlaceholderImg?.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /** 获取占位符控件 */
    fun getPlaceholderImageView(): ImageView = mPlaceholderImg!!

    /** 设置长图资源[resId] */
    fun setImageRes(@DrawableRes resId: Int) {
        Observable.just(resId)
                .map { id ->
                    return@map BitmapFactory.decodeResource(context.resources, id)
                }.compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {}
                })
    }

    /** 设置长图文件[file] */
    fun setImageFile(file: File) {
        Observable.just(file)
                .map { f ->
                    return@map BitmapFactory.decodeFile(f.absolutePath)
                }.compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {}
                })
    }

    /** 设置长图位图[bitmap] */
    fun setImageBitmap(bitmap: Bitmap) {
        Observable.just(bitmap)
                .map { bmp ->
                    return@map BitmapUtils.createLongLargeBitmaps(context, bmp)
                }.compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<List<Bitmap>>() {
                    override fun onBaseNext(any: List<Bitmap>) {
                        loadImageBitmaps(any)
                    }

                    override fun onBaseError(e: Throwable) {}
                })
    }

    /** 加载位图列表[bitmaps] */
    private fun loadImageBitmaps(bitmaps: List<Bitmap>) {
        for (bitmap in bitmaps) {
            val imageView = ImageView(context)
            imageView.adjustViewBounds = true
            imageView.setImageBitmap(bitmap)
            mRootLayout?.addView(imageView, ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        }
        mPlaceholderImg?.visibility = View.GONE
        mRootLayout?.visibility = View.VISIBLE
    }


}