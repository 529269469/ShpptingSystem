package com.fhxh.shpptingsystem.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fhxh.shpptingsystem.R
import com.fhxh.shpptingsystem.room.entity.BulletBean
import java.lang.String
import kotlin.Int

class HistoryCountAdapter(data: MutableList<BulletBean>):
    BaseQuickAdapter<BulletBean, BaseViewHolder>(R.layout.history_list_item, data)  {

    private var SelectIndex = -1
    private val MaxDataIndex = 10
    override fun convert(holder: BaseViewHolder, item: BulletBean) {
        holder.setText(R.id.history_list_item_number, String.valueOf(item.number))

        if (SelectIndex == holder.adapterPosition) {
            holder.setBackgroundResource(R.id.history_list_item_number, R.drawable.data)
        } else {
            holder.setBackgroundResource(R.id.history_list_item_number, R.drawable.charts)
        }
    }
    fun getSelectIndex(): Int {
        return SelectIndex
    }

    fun setSelectIndex(selectIndex: Int) {
        SelectIndex = selectIndex
        notifyDataSetChanged()
    }

}