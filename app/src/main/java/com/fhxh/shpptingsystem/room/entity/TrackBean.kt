package com.fhxh.shpptingsystem.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
class TrackBean {
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null //id
    var bulletid: Long? = null //子弹id

    var track_point_x:Float?=null
    var track_point_y:Float?=null

    constructor(uid: Long?, bulletid: Long?, track_point_x: Float?, track_point_y: Float?) {
        this.uid = uid
        this.bulletid = bulletid
        this.track_point_x = track_point_x
        this.track_point_y = track_point_y
    }
}