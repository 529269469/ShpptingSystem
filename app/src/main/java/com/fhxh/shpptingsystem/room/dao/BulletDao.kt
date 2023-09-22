package com.fhxh.shpptingsystem.room.dao

import androidx.room.*
import com.fhxh.shpptingsystem.room.entity.BulletBean

@Dao
interface BulletDao {
    @Query("SELECT * FROM bullet")
    fun getAll(): List<BulletBean>

    @Query("SELECT * FROM bullet WHERE bureau_id = :bureau_id")
    fun findBulletList(bureau_id: Long): List<BulletBean>

    @Query("SELECT * FROM bullet WHERE uid = :uid")
    fun findBulletById(uid: Long): BulletBean

    @Insert
    fun insertAll(vararg bullet: BulletBean)

    @Delete
    fun delete(bullet: BulletBean)

    @Update
    fun update(bullet: BulletBean)
}