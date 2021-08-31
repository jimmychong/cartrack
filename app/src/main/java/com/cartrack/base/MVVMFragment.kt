package com.cartrack.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cartrack.model.ErrorResponse

/**
 * Fragment for MVVM
 * @see <a href="https://developer.android.com/jetpack/docs/guide</a>
 */
abstract class MVVMFragment<VM : BaseViewModel, BINDING : ViewDataBinding> : BaseFragment() {
    protected lateinit var viewModel: VM
    protected lateinit var binding: BINDING

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModelInstance()

        viewModel.isShowLoading.observe(this) {
            showLoadingIndicator(it == true)
        }

        viewModel.apiError.observe(this) {
            handleApiError(it)
        }
    }

    protected open fun handleApiError(error: ErrorResponse) {
        baseActivity.handleApiError(error)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        /**
         * Dispose all disposables
         * e.g. disposable of api call
         */
        viewModel.getDisposableList().forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun setMainLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)

        setBindingData()

        return binding.root
    }

    abstract fun getViewModelInstance(): VM

    abstract fun setBindingData()
}