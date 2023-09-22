package com.fhxh.shpptingsystem.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 子弹路径——弹着点
 */
@Entity(tableName = "bullet")
class BulletBean {
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null //子弹id

    var bureau_id: Long? = null //局id
    var number: Int? = null //发序
    var cylinder_number: Double? = null //环数
    var direction: Int? = null //方向
    var data_time: String? = null //用时
    var time: String? = null //时间


    var bullet_point_x:Float?=null
    var bullet_point_y:Float?=null

    var ju_qiang:Int?=null //据枪
    var miao_zhun:Int?=null //瞄准
    var ji_fa:Int?=null //击发
    var cheng_ji:Int?=null //成绩
    var zong_ti:Int?=null //总体

    constructor(
        uid: Long?,
        bureau_id: Long?,
        number: Int?,
        cylinder_number: Double?,
        direction: Int?,
        data_time: String?,
        time: String?,
        bullet_point_x: Float?,
        bullet_point_y: Float?,
        ju_qiang: Int?,
        miao_zhun: Int?,
        ji_fa: Int?,
        cheng_ji: Int?,
        zong_ti: Int?
    ) {
        this.uid = uid
        this.bureau_id = bureau_id
        this.number = number
        this.cylinder_number = cylinder_number
        this.direction = direction
        this.data_time = data_time
        this.time = time
        this.bullet_point_x = bullet_point_x
        this.bullet_point_y = bullet_point_y
        this.ju_qiang = ju_qiang
        this.miao_zhun = miao_zhun
        this.ji_fa = ji_fa
        this.cheng_ji = cheng_ji
        this.zong_ti = zong_ti
    }
}