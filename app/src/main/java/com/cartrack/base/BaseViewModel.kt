package com.cartrack.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cartrack.model.ErrorResponse
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    val isShowLoading: MutableLiveData<Boolean> = MutableLiveData()
    protected val disposableList: ArrayList<Disposable> = arrayListOf()
    val apiError = MutableLiveData<ErrorResponse>()

    fun showLoadingIndicator(showLoading: Boolean) {
        isShowLoading.postValue(showLoading)
    }

    fun getDisposableList(): List<Disposable> = disposableList
}