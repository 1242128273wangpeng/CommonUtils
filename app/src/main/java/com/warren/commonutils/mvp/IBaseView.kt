package com.shouji2345.base


interface IBaseView<T> {

    fun showLoading()

    fun hideLoading()

    fun showToast(msg: String)

    fun showError(msg: String? = null)

    fun onSuccess(data: T?)

}