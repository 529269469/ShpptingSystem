package com.fhxh.shpptingsystem.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bureau")
class BureauBean {
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null //局id
    var num: Int? = null //局数
    var userId:Long?=null//人员id
    var total_ring_number:Double?=null //总环数
    var sum_up_send:Int?=null //总发数
    var qiang_id:String?=null//枪型
    var type:String?=null //射击类型
    var data_time:String?=null //时间
    var ba_mian:String?=null //靶面
    var year:String?=null //年
    var month:String?=null //月
    var day:String?=null //日

    constructor(
        uid: Long?,
        num: Int?,
        userId: Long?,
        total_ring_number: Double?,
        sum_up_send: Int?,
        qiang_id: String?,
        type: String?,
        data_time: String?,
        ba_mian: String?,
        year: String?,
        month: String?,
        day: String?
    ) {
        this.uid = uid
        this.num = num
        this.userId = userId
        this.total_ring_number = total_ring_number
        this.sum_up_send = sum_up_send
        this.qiang_id = qiang_id
        this.type = type
        this.data_time = data_time
        this.ba_mian = ba_mian
        this.year = year
        this.month = month
        this.day = day
    }
}