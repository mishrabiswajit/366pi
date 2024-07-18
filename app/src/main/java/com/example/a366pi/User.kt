package com.example.a366pi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    var username: String,
    var name: String,
    var password: String
)
