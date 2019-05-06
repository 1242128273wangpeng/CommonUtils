package com.shouji2345.imageloader


import com.facebook.drawee.view.SimpleDraweeView

class ImageLoaderManager private constructor() : ImageLoader {
    private var imageLoader: ImageLoaderStrategy = FrescoImageLoaderStrategy()
    private var options: ImageLoaderOptions = ImageLoaderOptions.Builder().build()

    companion object {
        @JvmStatic
        val instance: ImageLoaderManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageLoaderManager()
        }
    }

    override fun showImage(mView: SimpleDraweeView, mUrl: String) {
        options.setCrossFade(true)
        imageLoader.showImage(mView, mUrl, options)
    }

    override fun showImage(mView: SimpleDraweeView, mUrl: String, placeHolder: Int) {
        options.setCrossFade(true)
        options.setPlaceHolder(placeHolder)
        imageLoader.showImage(mView, mUrl, options)
    }

    override fun showLocalImage(mView: SimpleDraweeView, mDrawables: Int) {
        options.setCrossFade(true)
        imageLoader.showImage(mView, mDrawables, options)
    }

    override fun showLocalGif(mView: SimpleDraweeView, mDrawables: Int) {
        options.setCrossFade(true)
        options.setGif(true)
        imageLoader.showImage(mView, mDrawables, options)
    }

}