package com.shouji2345.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.shouji2345.R
import com.shouji2345.lib.base.common.BaseFragmentActivity
import com.shouji2345.utils.ToastUtils
import com.shouji2345.widget.NetImageView

abstract class BaseActivity<P : BasePresenter<*>, T> : BaseFragmentActivity(), IBaseView<T> {
    private var mPresenter: P? = null
    private lateinit var mLayoutInflater: LayoutInflater
    private lateinit var root: ViewGroup

    abstract fun createPresenter(): P


    fun getPresenter(): P {
        if (mPresenter == null) {
            mPresenter = createPresenter()
        }
        return mPresenter!!
    }

    override fun showLoading() {
        root.findViewById<LinearLayout>(R.id.llNetError).visibility = View.GONE
        root.findViewById<FrameLayout>(R.id.flLoading).visibility = View.VISIBLE
        root.findViewById<NetImageView>(R.id.imgLoading).visibility = View.VISIBLE
        root.findViewById<NetImageView>(R.id.imgLoading).loadLocalGif(R.drawable.app_assets_loading)
    }

    override fun hideLoading() {
        root.findViewById<NetImageView>(R.id.imgLoading).visibility = View.GONE
        root.findViewById<FrameLayout>(R.id.flLoading).visibility = View.GONE
    }

    override fun showToast(msg: String) {
        ToastUtils.showToast(msg)
    }

    override fun onSuccess(data: T?) {
        hideLoading()
        root.findViewById<LinearLayout>(R.id.llNetError).visibility = View.GONE
        root.findViewById<FrameLayout>(R.id.flContent).visibility = View.VISIBLE
    }

    override fun showError(msg: String?) {
        root.findViewById<FrameLayout>(R.id.flContent).visibility = View.GONE
        root.findViewById<NetImageView>(R.id.imgLoading).visibility = View.GONE
        root.findViewById<TextView>(R.id.tvNetErrorTip).text = msg
                ?: resources.getString(R.string.error_retry)
        root.findViewById<LinearLayout>(R.id.llNetError).visibility = View.VISIBLE
        ToastUtils.showToast(resources.getString(R.string.error_toast))
        root.findViewById<LinearLayout>(R.id.llNetError).setOnClickListener {
            getPresenter().retryFetch()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLayoutInflater = LayoutInflater.from(this)
        root = mLayoutInflater.inflate(R.layout.layout_base, null, false) as ViewGroup
        setContentView(layoutResID())
        initData()
    }

    abstract fun initData()

    @LayoutRes
    abstract fun layoutResID(): Int


    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        val content = mLayoutInflater.inflate(layoutResID, null, false)
        root.findViewById<FrameLayout>(R.id.flContent).removeAllViews()
        root.findViewById<FrameLayout>(R.id.flContent).addView(content)
    }

    override fun onDestroy() {
        hideLoading()
        mPresenter?.detachView()
        mPresenter?.dispose()
        super.onDestroy()
    }
}