package com.example.mynote.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "users_table", indices = [Index(value = ["user_name"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,

    @ColumnInfo(name = "user_name")
    var userName: String ,

    @ColumnInfo(name = "user_password")
    var userPassword: String,

    @ColumnInfo(name = "keep_me_signed")
    var keepMeSigned: Int = 0
)
