package com.cartrack.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cartrack.LoginAccountTableName
import com.cartrack.model.login.LoginAccount

@Dao
interface LoginAccountDao {
    @Query("SELECT * FROM $LoginAccountTableName")
    fun getDefaultAccount(): List<LoginAccount>

    @Query("SELECT * FROM $LoginAccountTableName WHERE userPhoneNumber = :userPhoneNumber AND  countryCode = :countryCode")
    fun isAccountExits(countryCode: Int, userPhoneNumber: String): LoginAccount?

    @Insert
    fun insertLoginAccount(loginAccount: LoginAccount)
}