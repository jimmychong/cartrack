package com.cartrack.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.cartrack.LoginAccountTableName
import com.cartrack.MainApplication
import com.cartrack.R
import com.cartrack.base.MVVMActivity
import com.cartrack.databinding.ActivityLoginBinding
import com.cartrack.model.login.LoginAccount
import com.cartrack.room.LoginAccountDatabase
import com.cartrack.util.LoginManager.isLogin
import com.cartrack.util.PasswordEncryption
import com.cartrack.util.PreferencesUtil
import com.cartrack.view.user.UserActivity
import com.cartrack.viewmodel.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : MVVMActivity<LoginViewModel, ActivityLoginBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showLoadingIndicator(true)
        CoroutineScope(Dispatchers.IO).launch {
            val accountDb =
                MainApplication.getDatabase<LoginAccountDatabase>(
                    applicationContext,
                    LoginAccountTableName
                )
            val dao = accountDb.loginAccountDao()
            if (dao.getDefaultAccount().isEmpty()) {
                dao.insertLoginAccount(
                    LoginAccount(
                        "75647488",
                        countryCode= 852,
                        PasswordEncryption.encryptBody("12345678")
                    )
                )
            }

            accountDb.close()
            showLoadingIndicator(false)
        }

        PreferencesUtil.getAccount(this)?.let {
            viewModel.rememberMe.set(true)
            viewModel.phone.set(it.userPhoneNumber)
            viewModel.password.set(PasswordEncryption.decryptBody(it.password.orEmpty()))
            binding.countryCodePicker.setCountryForPhoneCode(it.countryCode)
        }

        //set default country
        viewModel.country.set(binding.countryCodePicker.selectedCountryCode.toInt())

        binding.countryCodePicker.setOnCountryChangeListener { viewModel.country.set(binding.countryCodePicker.selectedCountryCode.toInt()) }

        viewModel.userInfo.observe(this) { userAccount ->
            if (viewModel.rememberMe.get() == true) {
                PreferencesUtil.saveRememberMeAccount(this, userAccount)
            } else {
                PreferencesUtil.deleteAccount(this)
            }
            isLogin = true
            startActivity(Intent(this, UserActivity::class.java))
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun getMainFragmentContainer(): Int? {
        return null
    }

    override fun getViewModelInstance(): LoginViewModel {
        return LoginViewModel()
    }

    override fun setBindingData() {
        binding.viewModel = viewModel
        binding.view = this
    }

    fun startLogin() {
        viewModel.verifyAccount(MainApplication.getDatabase(this, LoginAccountTableName))
    }
}