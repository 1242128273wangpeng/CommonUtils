package com.shouji2345.imageloader

import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.common.util.UriUtil.LOCAL_RESOURCE_SCHEME
import android.R.attr.scheme
import android.R.attr.path
import android.net.Uri
import com.facebook.common.util.UriUtil


class FrescoImageLoaderStrategy : ImageLoaderStrategy {
    override fun showImage(v: SimpleDraweeView, drawable: Int, options: ImageLoaderOptions) {
        val hierarchyBuilder = GenericDraweeHierarchyBuilder(v.resources)
        if (options.getPlaceHolder() > 0) {
            hierarchyBuilder.setPlaceholderImage(options.getPlaceHolder())
        }
        if (options.getErrorDrawable() > 0) {
            hierarchyBuilder.setPlaceholderImage(options.getErrorDrawable())
        }
        if (options.isCrossFade()) {
            hierarchyBuilder.fadeDuration = 300
        }
        val uri = Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(drawable.toString())
                .build()
        val controllerBuilder = Fresco.newDraweeControllerBuilder()
                .setOldController(v.controller)
                .setUri(uri)
        if (options.isGif()) {
            controllerBuilder.autoPlayAnimations = true
        }
        val controller = controllerBuilder.build()
        controller.hierarchy = hierarchyBuilder.build()
        v.controller = controller
    }

    override fun showImage(v: SimpleDraweeView, url: String, options: ImageLoaderOptions) {
        val hierarchyBuilder = GenericDraweeHierarchyBuilder(v.resources)
        if (options.getPlaceHolder() > 0) {
            hierarchyBuilder.setPlaceholderImage(options.getPlaceHolder())
        }
        if (options.getErrorDrawable() > 0) {
            hierarchyBuilder.setPlaceholderImage(options.getErrorDrawable())
        }
        if (options.isCrossFade()) {
            hierarchyBuilder.fadeDuration = 300
        }
        val imageRequestBuilder = ImageRequestBuilder.fromRequest(ImageRequest.fromUri(url))
        options.getSize()?.takeIf { it.reWidth > 0 && it.reHeight > 0 }?.run {
            imageRequestBuilder.setResizeOptions(ResizeOptions.forDimensions(reHeight, reHeight))
        }
        val controllerBuilder = Fresco.newDraweeControllerBuilder()
                .setOldController(v.controller)
                .setImageRequest(imageRequestBuilder.build())
        val controller = controllerBuilder.build()
        controller.hierarchy = hierarchyBuilder.build()
        v.controller = controller
    }
}