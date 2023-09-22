package com.fhxh.shpptingsystem.room.dao

import androidx.room.*
import com.fhxh.shpptingsystem.room.entity.TrackBean

@Dao
interface TrackDao {
    @Query("SELECT * FROM track")
    fun getAll(): List<TrackBean>

    @Query("SELECT * FROM track WHERE bulletid = :bulletid ")
    fun findByTrack(bulletid: Long): List<TrackBean>

    @Insert
    fun insertAll(vararg track: TrackBean)

    @Delete
    fun delete(track: TrackBean)

    @Update
    fun update(track: TrackBean)


}