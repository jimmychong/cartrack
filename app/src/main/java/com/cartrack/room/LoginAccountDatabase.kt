package com.cartrack.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cartrack.model.login.LoginAccount

@Database(entities = arrayOf(LoginAccount::class),version = 2)
abstract class LoginAccountDatabase : RoomDatabase() {
    abstract fun loginAccountDao(): LoginAccountDao
}