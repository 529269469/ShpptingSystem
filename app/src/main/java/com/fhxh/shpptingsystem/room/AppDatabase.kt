package com.fhxh.shpptingsystem.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fhxh.shpptingsystem.room.dao.BulletDao
import com.fhxh.shpptingsystem.room.dao.BureauDao
import com.fhxh.shpptingsystem.room.dao.TrackDao

import com.fhxh.shpptingsystem.room.dao.UserDao
import com.fhxh.shpptingsystem.room.entity.BulletBean
import com.fhxh.shpptingsystem.room.entity.BureauBean
import com.fhxh.shpptingsystem.room.entity.TrackBean
import com.fhxh.shpptingsystem.room.entity.User

@Database(entities = [User::class,BulletBean::class,BureauBean::class,TrackBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun bulletDao(): BulletDao

    abstract fun bureauDao(): BureauDao

    abstract fun trackDao(): TrackDao



}

