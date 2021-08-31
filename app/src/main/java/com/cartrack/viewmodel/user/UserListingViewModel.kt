package com.cartrack.viewmodel.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cartrack.base.BaseViewModel
import com.cartrack.model.ErrorResponse
import com.cartrack.model.user.UserInfo
import com.cartrack.network.ApiManager
import com.cartrack.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class UserListingViewModel : BaseViewModel() {
    var userStatusList = MutableLiveData<List<UserInfo>>()

    init {
        getUserList()
    }

    fun getUserList(){


        disposableList.add(
            ApiManager.create<NetworkService>().getUserData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showLoadingIndicator(false)
                    Log.d("DEBUG", "CALL RESULT $it")
                    if (it.isNotEmpty()) {
                        userStatusList.value = it
                    } else {
                        apiError.value = ErrorResponse("No data")
                    }
                }, {
                    showLoadingIndicator(false)
                    apiError.value = ErrorResponse("Call Fail: ${it.message}")
                })
        )
    }
}