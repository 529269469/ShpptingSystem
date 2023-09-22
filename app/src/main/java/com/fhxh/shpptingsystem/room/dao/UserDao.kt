package com.fhxh.shpptingsystem.room.dao

import androidx.room.*
import com.fhxh.shpptingsystem.room.entity.User


@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE uid = :userId LIMIT 1")
    fun loadAllById(userId: Long): User

    @Query("SELECT * FROM users WHERE user_name = :user_name LIMIT 1")
    fun findByName(user_name: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)
}