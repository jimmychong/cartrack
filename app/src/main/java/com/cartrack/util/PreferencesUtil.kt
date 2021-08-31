package com.cartrack.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cartrack.localPrefFileName
import com.cartrack.model.login.LoginAccount
import com.cartrack.prefAccountKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.security.MessageDigest
import java.util.Locale
import java.util.concurrent.TimeUnit

object PreferencesUtil {

    fun saveRememberMeAccount(context: Context?, account: LoginAccount) {
        val localPref = context?.getSharedPreferences(localPrefFileName, Context.MODE_PRIVATE)
        localPref?.edit()?.apply {
            putString(prefAccountKey, Gson().toJson(account))
            apply()
        }
    }

    fun getAccount(context: Context?): LoginAccount? {
        if (context == null) return null
        val localPref = context.getSharedPreferences(localPrefFileName, Context.MODE_PRIVATE)
        val accountJsonString = localPref.getString(prefAccountKey, "")

        return if (accountJsonString.isNullOrEmpty()) {
            null
        } else {
            Gson().fromJson(accountJsonString, LoginAccount::class.java)
        }
    }

    fun deleteAccount(context: Context?){
        context?.let{
            val localPref = context.getSharedPreferences(localPrefFileName, Context.MODE_PRIVATE)
            localPref.edit().clear().commit()
        }
    }

}