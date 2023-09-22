package com.fhxh.shpptingsystem.ui.adapter.provider;

import android.view.View;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.ui.adapter.tree.FifthNode;
import com.fhxh.shpptingsystem.ui.adapter.tree.HistoryMessage;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

/**
 * Created by  on 2021/7/10 11:17.
 */
public class FifthProvider extends BaseNodeProvider {

    private FifthNode SelectData = null;
    @Override
    public int getItemViewType() {
        return 5;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_node_fifth;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        FifthNode entity = (FifthNode) data;
        helper.setText(R.id.title, entity.getTitle() + "局");
        if(SelectData != null){
            if(SelectData.getTitle().equals(entity.getTitle()) && SelectData.getPerson().equals(entity.getPerson())){
                helper.setBackgroundResource(R.id.item_node_fifth_linear, R.drawable.data);
            }
            else{
                helper.setBackgroundResource(R.id.item_node_fifth_linear, R.drawable.a);
            }
        }
        else{
            helper.setBackgroundResource(R.id.item_node_fifth_linear, R.drawable.a);
        }
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        if(SelectData != null){
            if(!SelectData.getTitle().equals(((FifthNode) data).getTitle()) || !SelectData.getPerson().equals(((FifthNode) data).getPerson())){
                SelectData = (FifthNode) data;
                HistoryMessage historyMessage = new HistoryMessage(
                        SelectData.getPerson(),
                        SelectData.getTitle(),SelectData.getId()
                );
                EventBus.getDefault().post(historyMessage);
                getAdapter().notifyDataSetChanged();
            }
        }
        else{
            SelectData = (FifthNode) data;
            HistoryMessage historyMessage = new HistoryMessage(
                    SelectData.getPerson(),
                    SelectData.getTitle(),SelectData.getId()
            );
            EventBus.getDefault().post(historyMessage);
            getAdapter().notifyDataSetChanged();
        }
    }

}
