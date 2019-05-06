package com.shouji2345.mvp

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.shouji2345.R
import com.shouji2345.base.BasePresenter
import com.shouji2345.base.IBaseView
import com.shouji2345.utils.ToastUtils
import com.shouji2345.widget.NetImageView

abstract class BaseFragment<P : BasePresenter<*>, T> : Fragment(), IBaseView<T> {
    private var mPresenter: P? = null
    private lateinit var fragmentRoot: ViewGroup

    abstract fun createPresenter(): P

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentRoot = inflater.inflate(R.layout.layout_base, container, false) as ViewGroup
        val content = inflater.inflate(layoutResID(), null, false)
        fragmentRoot.findViewById<FrameLayout>(R.id.flContent).removeAllViews()
        fragmentRoot.findViewById<FrameLayout>(R.id.flContent).addView(content)
        return fragmentRoot
    }

    @LayoutRes
    abstract fun layoutResID(): Int

    fun getPresenter(): P {
        if (mPresenter == null) {
            mPresenter = createPresenter()
        }
        return mPresenter!!
    }

    override fun showLoading() {
        fragmentRoot.findViewById<LinearLayout>(R.id.llNetError).visibility = View.GONE
        fragmentRoot.findViewById<FrameLayout>(R.id.flLoading).visibility = View.VISIBLE
        fragmentRoot.findViewById<NetImageView>(R.id.imgLoading).visibility = View.VISIBLE
        fragmentRoot.findViewById<NetImageView>(R.id.imgLoading).loadLocalGif(R.drawable.app_assets_loading)
    }

    override fun hideLoading() {
        fragmentRoot.findViewById<NetImageView>(R.id.imgLoading).visibility = View.GONE
        fragmentRoot.findViewById<FrameLayout>(R.id.flLoading).visibility = View.GONE
    }

    override fun showToast(msg: String) {
        ToastUtils.showToast(msg)
    }

    override fun showError(msg: String?) {
        hideLoading()
        fragmentRoot.findViewById<FrameLayout>(R.id.flContent).visibility = View.GONE
        fragmentRoot.findViewById<LinearLayout>(R.id.llNetError).visibility = View.VISIBLE
        fragmentRoot.findViewById<TextView>(R.id.tvNetErrorTip).text = msg ?: resources.getString(R.string.error_retry)
        ToastUtils.showToast(resources.getString(R.string.error_toast))
        fragmentRoot.findViewById<LinearLayout>(R.id.llNetError).setOnClickListener {
            getPresenter().retryFetch()
        }
    }

    override fun onSuccess(data: T?) {
        hideLoading()
        fragmentRoot.findViewById<LinearLayout>(R.id.llNetError).visibility = View.GONE
        fragmentRoot.findViewById<FrameLayout>(R.id.flContent).visibility = View.VISIBLE
    }

    override fun onDestroy() {
        hideLoading()
        mPresenter?.detachView()
        mPresenter?.dispose()
        super.onDestroy()
    }
}