package com.cartrack

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cartrack.model.login.LoginAccount
import com.cartrack.room.LoginAccountDatabase
import com.cartrack.util.PasswordEncryption

class MainApplication : Application() {
    val networkConnected: MutableLiveData<Boolean> = MutableLiveData()


    companion object{
        inline fun <reified T : RoomDatabase> getDatabase(
            context: Context,
            databaseName: String,
        ): T {
            val dbBuilder = Room.databaseBuilder(context, T::class.java, databaseName).fallbackToDestructiveMigration()

            return dbBuilder.build()
        }

    }
    private val connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            networkConnected.postValue(true)
        }

        override fun onLost(network: Network) {
            networkConnected.postValue(false)
        }
    }

    override fun onCreate() {
        super.onCreate()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

    }
}