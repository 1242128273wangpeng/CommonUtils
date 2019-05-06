package com.warren.commonutils.imageloader

import android.content.Context
import android.util.AttributeSet
import com.facebook.drawee.view.SimpleDraweeView
import com.shouji2345.imageloader.ImageLoaderManager

class NetImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SimpleDraweeView(context, attrs, defStyleAttr) {
    fun loadImage(url: String) {
        ImageLoaderManager.instance.showImage(this, url)
    }

    fun loadLocalImage(resId: Int) {
        ImageLoaderManager.instance.showLocalImage(this, resId)
    }

    fun loadLocalGif(resId: Int) {
        ImageLoaderManager.instance.showLocalGif(this, resId)
    }
}