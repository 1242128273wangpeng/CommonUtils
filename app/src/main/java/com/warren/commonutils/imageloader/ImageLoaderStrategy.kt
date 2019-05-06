package com.shouji2345.imageloader

import com.facebook.drawee.view.SimpleDraweeView

interface ImageLoaderStrategy {
    fun showImage(v: SimpleDraweeView, url: String, options: ImageLoaderOptions)
    fun showImage(v: SimpleDraweeView, drawable: Int, options: ImageLoaderOptions)
}