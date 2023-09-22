package com.fhxh.shpptingsystem.room.dao

import androidx.room.*
import com.fhxh.shpptingsystem.room.entity.BureauBean

@Dao
interface BureauDao {
    @Query("SELECT * FROM bureau")
    fun getAll(): List<BureauBean>

    @Query("SELECT * FROM bureau WHERE userId = :userId ")
    fun findBureauId(userId: Long): List<BureauBean>

    @Query("SELECT * FROM bureau WHERE uid = :uid LIKE 1")
    fun findById(uid: Long): BureauBean

    @Query("SELECT * FROM bureau WHERE year = :year AND month =:month AND day =:day")
    fun findBureauDay(year: String, month: String, day: String): List<BureauBean>

    @Query("SELECT * FROM bureau WHERE year = :year AND month =:month AND day =:day AND userId = :userId")
    fun findBureau(year: String, month: String, day: String,userId: Long): List<BureauBean>

    @Query("SELECT * FROM bureau WHERE year = :year")
    fun findBureauYear(year: String): List<BureauBean>

    @Query("SELECT * FROM bureau WHERE year = :year AND month =:month ")
    fun findBureauMonth(year: String,month: String): List<BureauBean>

    @Insert
    fun insertAll(vararg bureau: BureauBean)

    @Delete
    fun delete(bureau: BureauBean)

    @Update
    fun update(bureau: BureauBean)

}