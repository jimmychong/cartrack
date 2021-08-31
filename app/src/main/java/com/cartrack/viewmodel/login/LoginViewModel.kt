package com.cartrack.viewmodel.login

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.cartrack.ValidPasswordLength
import com.cartrack.ValidPhoneLength
import com.cartrack.base.BaseViewModel
import com.cartrack.model.ErrorResponse
import com.cartrack.model.login.LoginAccount
import com.cartrack.room.LoginAccountDatabase
import com.cartrack.util.PasswordEncryption
import kotlinx.coroutines.*

class LoginViewModel : BaseViewModel() {
    val enableLogin = ObservableBoolean()

    //save login account
    val phone = ObservableField<String>()
    val password = ObservableField<String>()
    val rememberMe = ObservableField<Boolean>()
    val country = ObservableField<Int>()

    val userInfo = MutableLiveData<LoginAccount>()

    init {
        //default set login button disable
        enableLogin.set(false)
    }

    fun onPhoneChanged(emailInput: CharSequence, start: Int, before: Int, count: Int) {
        val input = emailInput.toString()
        phone.set(input)
        enableLogin.set(
            phone.get().orEmpty().length >= ValidPhoneLength && password.get()
                .orEmpty().length >= ValidPasswordLength
        )
    }

    fun onPasswordChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val input = s.toString()
        password.set(input)
        enableLogin.set(
            phone.get().orEmpty().length >= ValidPhoneLength && password.get()
                .orEmpty().length >= ValidPasswordLength
        )
    }

    fun verifyAccount(loginAccountDb: LoginAccountDatabase) {
        showLoadingIndicator(true)
        CoroutineScope(Dispatchers.IO).launch {
            val fullPhoneNumber = "${country.get()}${phone.get()}"
            Log.d("DEBUG", "FULL PHONE $fullPhoneNumber")
            val loginAccount = loginAccountDb.loginAccountDao().isAccountExits(country.get() ?: 0,phone.get().orEmpty())
            loginAccountDb.close()
            withContext(Dispatchers.Main) {
                loginAccount?.let {
                    if (PasswordEncryption.encryptBody(
                            password.get().orEmpty()
                        ) == loginAccount.password
                    ) {
                        userInfo.value = it
                    } else {
                        //now use api error call to handle
                        apiError.value = ErrorResponse("Phone or Password is incorrect")
                    }
                } ?: run {
                    //cannot find this phone number in db
                    apiError.value = ErrorResponse("No user record")
                }

                showLoadingIndicator(false)
            }

        }

    }


}