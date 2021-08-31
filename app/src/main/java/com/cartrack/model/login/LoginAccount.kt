package com.cartrack.model.login

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cartrack.LoginAccountTableName

@Entity(tableName = LoginAccountTableName)
data class LoginAccount(
    @PrimaryKey var userPhoneNumber: String,
    var countryCode: Int = 0,
    var password: String? = null
)
