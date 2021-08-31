package com.cartrack.network

import com.cartrack.model.user.UserInfo
import io.reactivex.Observable
import retrofit2.http.GET

const val getUserData = "users"
interface NetworkService {
    @GET(getUserData)
    fun getUserData(): Observable<List<UserInfo>>
}