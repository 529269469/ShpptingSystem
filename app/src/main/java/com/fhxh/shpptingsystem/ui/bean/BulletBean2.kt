package com.fhxh.shpptingsystem.ui.bean

data class BulletBean2(
    var BulletBean:Int=200,
    var ip: String? = null,
    var bureau_id: Long? = null,//局id
    var number: Int? = null,//发序
    var cylinder_number: Double? = null,//环数
    var cylinder_number2: Double? = null,//环数
    var direction: Int? = null,//方向
    var data_time: String? = null,//用时
    var time: String? = null,//时间
    var x: Float? = null,
    var y: Float? = null,
    var ju_qiang: Int? = null,//据枪
    var miao_zhun: Int? = null,//瞄准
    var ji_fa: Int? = null, //击发
    var cheng_ji: Int? = null,//成绩
    var zong_ti: Int? = null //总体
)


