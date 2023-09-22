package com.fhxh.shpptingsystem.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Long ?=null,
    var user_name: String?=null,
    var password: String?=null,
    var path: String?=null,
)

