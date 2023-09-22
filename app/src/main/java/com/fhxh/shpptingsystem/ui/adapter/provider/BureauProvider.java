package com.fhxh.shpptingsystem.ui.adapter.provider;

import android.view.View;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.ui.adapter.tree.BureauNode;
import com.fhxh.shpptingsystem.ui.adapter.tree.HistoryMessage;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by  on 2021/7/10 14:47.
 */
public class BureauProvider extends BaseNodeProvider {
    private BureauNode SelectData = null;
    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_node_bureau;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        BureauNode entity = (BureauNode) data;
        helper.setText(R.id.title, entity.getTitle() + "局");
        if(SelectData != null){
            if(SelectData.getTitle().equals(entity.getTitle()) && SelectData.getPerson().equals(entity.getPerson())){
                helper.setBackgroundResource(R.id.item_node_bureau_linear, R.drawable.data);
            }
            else{
                helper.setBackgroundResource(R.id.item_node_bureau_linear,R.drawable.a);
            }
        }
        else{
            helper.setBackgroundResource(R.id.item_node_bureau_linear, R.drawable.a);
        }
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        if(SelectData != null){
            if(!SelectData.getTitle().equals(((BureauNode) data).getTitle()) || !SelectData.getPerson().equals(((BureauNode) data).getPerson())){
                SelectData = (BureauNode) data;
                HistoryMessage historyMessage = new HistoryMessage(
                        SelectData.getPerson(),
                        SelectData.getTitle(),SelectData.getId()
                );
                EventBus.getDefault().post(historyMessage);
                getAdapter().notifyDataSetChanged();
            }
        }
        else{
            SelectData = (BureauNode) data;
            HistoryMessage historyMessage = new HistoryMessage(
                    SelectData.getPerson(),
                    SelectData.getTitle(),SelectData.getId()
            );
            EventBus.getDefault().post(historyMessage);
            getAdapter().notifyDataSetChanged();
        }
    }

    public boolean NextBureaus(int index, String bureaus){
        boolean istrue = false;
        List<BaseNode> list = getAdapter().getData().get(index).getChildNode();
        for (int i = 0; i < list.size(); i++) {
            BureauNode bureauNode = (BureauNode)list.get(i);
            if(String.valueOf(Integer.parseInt(bureaus) + 1).equals(bureauNode.getTitle())){
                SelectData = bureauNode;
                istrue = true;
            }
        }
        if(istrue){
            HistoryMessage historyMessage = new HistoryMessage(
                    SelectData.getPerson(),
                    SelectData.getTitle(),SelectData.getId()
            );
            EventBus.getDefault().post(historyMessage);

        }
        else{
            for (int i = 0; i < list.size(); i++) {
                BureauNode bureauNode = (BureauNode)list.get(i);
                if(bureaus.equals(bureauNode.getTitle())){
                    SelectData = bureauNode;
                }
            }
        }
        getAdapter().notifyDataSetChanged();
        return istrue;
    }

    public boolean LastBureaus(int index, String bureaus){
        boolean istrue = false;
        List<BaseNode> list = getAdapter().getData().get(index).getChildNode();
        for (int i = 0; i < list.size(); i++) {
            BureauNode bureauNode = (BureauNode)list.get(i);
            if(String.valueOf(Integer.parseInt(bureaus) - 1).equals(bureauNode.getTitle())){
                SelectData = bureauNode;
                istrue = true;
            }
        }
        if(istrue){
            HistoryMessage historyMessage = new HistoryMessage(
                    SelectData.getPerson(),
                    SelectData.getTitle(),SelectData.getId()
            );
            EventBus.getDefault().post(historyMessage);
        }
        else{
            for (int i = 0; i < list.size(); i++) {
                BureauNode bureauNode = (BureauNode)list.get(i);
                if(bureaus.equals(bureauNode.getTitle())){
                    SelectData = bureauNode;
                }
            }
        }
        getAdapter().notifyDataSetChanged();
        return istrue;
    }

}
