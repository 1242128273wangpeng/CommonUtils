package com.shouji2345.imageloader

import android.support.annotation.DrawableRes
import android.support.annotation.NonNull
import com.facebook.drawee.view.SimpleDraweeView

interface ImageLoader {
    /**
     * 加载网络图
     */
    fun showImage(@NonNull mView: SimpleDraweeView, @NonNull mUrl: String)

    /**
     * 加载网络图，附带placeHolder
     */
    fun showImage(@NonNull mView: SimpleDraweeView, @NonNull mUrl: String, @DrawableRes placeHolder: Int)

    /**
     * 加载本地图
     */
    fun showLocalImage(@NonNull mView: SimpleDraweeView, @NonNull @DrawableRes mDrawables: Int)

    /**
     * 加载本地动态
     */
    fun showLocalGif(@NonNull mView: SimpleDraweeView, @NonNull @DrawableRes mDrawables: Int)
}