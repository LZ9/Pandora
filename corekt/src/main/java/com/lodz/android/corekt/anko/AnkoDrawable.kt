package com.lodz.android.corekt.anko

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * Drawable扩展类
 * Created by zhouL on 2018/7/2.
 */

/** 用颜色资源[color]创建Drawable */
fun Context.createColorDrawable(@ColorRes color: Int): ColorDrawable = ColorDrawable(getColorCompat(color))

/** 用颜色资源[color]创建Drawable */
fun View.createColorDrawable(@ColorRes color: Int): ColorDrawable = ColorDrawable(getColorCompat(color))

/** 用颜色[color]创建Drawable */
fun Context.createColorIntDrawable(@ColorInt color: Int): ColorDrawable = ColorDrawable(color)

/** 用颜色[color]创建Drawable */
fun View.createColorIntDrawable(@ColorInt color: Int): ColorDrawable = ColorDrawable(color)

/** 用[bitmap]创建Drawable */
fun Context.createBitmapDrawable(bitmap: Bitmap): BitmapDrawable = BitmapDrawable(resources, bitmap)

/** 用[bitmap]创建Drawable */
fun View.createBitmapDrawable(bitmap: Bitmap): BitmapDrawable = BitmapDrawable(context.resources, bitmap)