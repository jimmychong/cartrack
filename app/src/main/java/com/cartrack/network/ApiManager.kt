package com.cartrack.network

import android.util.Log
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiManager {
    inline fun <reified T> create(retrofitEventListener: EventListener? = null): T {
        val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d("ApiManager", message) }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val clientBuilder = OkHttpClient.Builder()
//            .addInterceptor(ParseResponseInterceptor())
            .addInterceptor(loggingInterceptor)
            .apply {
                retrofitEventListener?.let {
                    eventListener(it)
                }
            }
            .connectTimeout(60000L, TimeUnit.MILLISECONDS)
            .readTimeout(60000L, TimeUnit.MILLISECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(clientBuilder)
            .build()

        return retrofit.create(T::class.java)
    }
}