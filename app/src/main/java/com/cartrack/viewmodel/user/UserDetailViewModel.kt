package com.cartrack.viewmodel.user

import androidx.databinding.ObservableField
import com.cartrack.base.BaseViewModel
import com.cartrack.model.user.UserInfo

class UserDetailViewModel : BaseViewModel() {
    var userInfo = ObservableField<UserInfo>()

}