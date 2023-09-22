package com.fhxh.shpptingsystem.ui.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fhxh.shpptingsystem.R
import com.fhxh.shpptingsystem.room.entity.BulletBean

class MainAdapter(data: MutableList<BulletBean>) :
    BaseQuickAdapter<BulletBean, BaseViewHolder>(R.layout.main_list_item, data) {


    override fun convert(baseViewHolder: BaseViewHolder, item: BulletBean) {
        baseViewHolder.setText(
            R.id.main_list_item_number,
            java.lang.String.valueOf(baseViewHolder.adapterPosition + 1)
        )
            .setText(R.id.main_list_item_currentTime, item.time)//时间
        //环数
        baseViewHolder.setText(
            R.id.main_list_item_currentRing,
            java.lang.String.valueOf(item.cylinder_number)
        )

        val direction: Int = item.direction!!
        when (direction) {
            1 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection1
            )
            2 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection2
            )
            3 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection3
            )
            4 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection4
            )
            5 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection5
            )
            6 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection6
            )
            7 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection7
            )
            8 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection8
            )
            9 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection9
            )
            10 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection10
            )
            11 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection11
            )
            12 -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection12
            )
            100 ->
                baseViewHolder.setImageResource(
                    R.id.main_list_item_direction,
                    R.mipmap.shootdirection100
                )
            else -> baseViewHolder.setImageResource(
                R.id.main_list_item_direction,
                R.mipmap.shootdirection13
            )
        }
        //用时
        baseViewHolder.setText(
            R.id.main_list_item_useTime,
            item.data_time
        )

        if (baseViewHolder.adapterPosition === selectPosition) {
            baseViewHolder.setBackgroundResource(R.id.main_list_item_main, R.drawable.data)
            baseViewHolder.setImageResource(R.id.main_list_item_replay, R.mipmap.trackplayback)
        } else {
            baseViewHolder.setBackgroundColor(R.id.main_list_item_main, Color.TRANSPARENT)
            baseViewHolder.setImageResource(R.id.main_list_item_replay, R.mipmap.trackplaybacknull)
        }
    }


    var selectPosition = -1


}
