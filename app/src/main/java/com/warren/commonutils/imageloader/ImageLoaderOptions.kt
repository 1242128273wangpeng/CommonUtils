package com.shouji2345.imageloader

import android.view.ViewPropertyAnimator

class ImageLoaderOptions {
    private var placeHolder = -1 //当没有成功加载的时候显示的图片
    private var errorDrawable = -1  //加载错误的时候显示的drawable
    private var isCrossFade = false //是否渐变平滑的显示图片
    private var isGif = false //是否Gif图片
    private var isSkipMemoryCache = false //是否跳过内存缓存
    private var size: ImageReSize? = null //重新设定容器宽高
    private var animator: ViewPropertyAnimator? = null // 图片加载动画

    constructor(resize: ImageReSize?, placeHolder: Int, errorDrawable: Int, isCrossFade: Boolean, isGif: Boolean, isSkipMemoryCache: Boolean, animator: ViewPropertyAnimator?) {
        this.placeHolder = placeHolder
        this.size = resize
        this.errorDrawable = errorDrawable
        this.isCrossFade = isCrossFade
        this.isGif = isGif
        this.isSkipMemoryCache = isSkipMemoryCache
        this.animator = animator
    }

    inner class ImageReSize(_reWidth: Int, _reHeight: Int) {
        internal var reWidth = 0
        internal var reHeight = 0

        init {
            this.reHeight = if (_reHeight < 0) 0 else _reHeight
            this.reWidth = if (_reWidth < 0) 0 else _reWidth
        }
    }


    fun getPlaceHolder(): Int {
        return placeHolder
    }

    fun setPlaceHolder(placeHolder: Int) {
        this.placeHolder = placeHolder
    }

    fun getSize(): ImageReSize? {
        return size
    }

    fun setSize(size: ImageReSize) {
        this.size = size
    }

    fun getErrorDrawable(): Int {
        return errorDrawable
    }

    fun setErrorDrawable(errorDrawable: Int) {
        this.errorDrawable = errorDrawable
    }

    fun isCrossFade(): Boolean {
        return isCrossFade
    }

    fun setCrossFade(crossFade: Boolean) {
        isCrossFade = crossFade
    }

    fun isSkipMemoryCache(): Boolean {
        return isSkipMemoryCache
    }

    fun setSkipMemoryCache(skipMemoryCache: Boolean) {
        isSkipMemoryCache = skipMemoryCache
    }

    fun getAnimator(): ViewPropertyAnimator? {
        return animator
    }

    fun isGif(): Boolean {
        return isGif
    }

    fun setGif(isGif: Boolean) {
        this.isGif = isGif
    }


    class Builder {
        private var placeHolder = -1
        private var size: ImageReSize? = null
        private var errorDrawable = -1
        private var isCrossFade = false
        private var isGif = false
        private var isSkipMemoryCache = false
        private var animator: ViewPropertyAnimator? = null
        fun placeHolder(drawable: Int): Builder {
            this.placeHolder = drawable
            return this
        }

        fun reSize(size: ImageReSize): Builder {
            this.size = size
            return this
        }

        fun anmiator(animator: ViewPropertyAnimator): Builder {
            this.animator = animator
            return this
        }

        fun errorDrawable(errorDrawable: Int): Builder {
            this.errorDrawable = errorDrawable
            return this
        }

        fun isCrossFade(isCrossFade: Boolean): Builder {
            this.isCrossFade = isCrossFade
            return this
        }

        fun isGif(isGif: Boolean): Builder {
            this.isGif = isGif
            return this
        }


        fun isSkipMemoryCache(isSkipMemoryCache: Boolean): Builder {
            this.isSkipMemoryCache = isSkipMemoryCache
            return this
        }

        fun build(): ImageLoaderOptions {
            return ImageLoaderOptions(this.size, this.placeHolder, this.errorDrawable, this.isCrossFade, this.isGif, this.isSkipMemoryCache, this.animator)
        }
    }
}