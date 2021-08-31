package com.cartrack.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Activity for MVVM
 * @see <a href="https://developer.android.com/jetpack/docs/guide</a>
 */
abstract class MVVMActivity<VM : BaseViewModel, BINDING : ViewDataBinding> : BaseActivity() {
    protected lateinit var viewModel: VM
    protected lateinit var binding: BINDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModelInstance()

        setBindingData()

        viewModel.isShowLoading.observe(this) {
            showLoadingIndicator(it == true)
        }

        viewModel.apiError.observe(this) {
            handleApiError(it)
        }
    }

    @CallSuper
    override fun onDestroy() {
        /**
         * Dispose all disposables
         */
        viewModel.getDisposableList().forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        super.onDestroy()
    }

    override fun setMainLayout() {
        binding = DataBindingUtil.setContentView(this, getLayoutResId())
    }

    abstract fun getViewModelInstance(): VM

    abstract fun setBindingData()
}