package com.shouji2345.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

abstract class BasePresenter<V : IBaseView<*>>(private val view: V) : IBasePresenter {
    private var mViewRef: WeakReference<V>? = null
    private val disposables = CompositeDisposable()

    init {
        attachView(view)
    }

    private fun attachView(view: V) {
        mViewRef = WeakReference(view)
    }

    fun getView(): V? {
        mViewRef = WeakReference(view)
        return if (isViewAttach()) {
            mViewRef?.get()
        } else {
            null
        }
    }

    protected fun addDisposable(dis: Disposable) {
        disposables.add(dis)
    }

    override fun isViewAttach(): Boolean {
        return mViewRef != null && mViewRef?.get() != null
    }

    fun dispose() {
        disposables.clear()
    }

    override fun detachView() {
        if (mViewRef != null) {
            mViewRef?.clear()
            mViewRef = null
        }
    }

    abstract fun loadData()

    open fun retryFetch() {
        loadData()
    }

}