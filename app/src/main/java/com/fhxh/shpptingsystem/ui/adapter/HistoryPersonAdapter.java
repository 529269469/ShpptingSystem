package com.fhxh.shpptingsystem.ui.adapter;


import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fhxh.shpptingsystem.ui.adapter.provider.BureauProvider;
import com.fhxh.shpptingsystem.ui.adapter.provider.PersonProvider;
import com.fhxh.shpptingsystem.ui.adapter.tree.BureauNode;
import com.fhxh.shpptingsystem.ui.adapter.tree.PersonNode;


import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by  on 2021/7/10 14:53.
 */
public class HistoryPersonAdapter extends BaseNodeAdapter {
    private PersonProvider personProvider = new PersonProvider();
    private BureauProvider bureauProvider = new BureauProvider();

    public HistoryPersonAdapter() {
        addNodeProvider(personProvider);
        addNodeProvider(bureauProvider);
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> list, int position) {
        BaseNode node = list.get(position);
        if (node instanceof PersonNode) {
            return 1;
        } else if (node instanceof BureauNode) {
            return 2;
        }
        return -1;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BaseNode item) {
        super.convert(holder, item);
    }

    public boolean NextBureaus(String personname, String bureau){
        int index = personProvider.SelectData(personname);
        boolean isture = false;
        if(index != -1){
            isture = bureauProvider.NextBureaus(index, bureau);
        }
        return isture;
    }

    public boolean LastBureaus(String personname, String bureau){
        int index = personProvider.SelectData(personname);
        boolean isture = false;
        if(index != -1){
            isture = bureauProvider.LastBureaus(index, bureau);
        }
        return isture;
    }

    public static final int EXPAND_COLLAPSE_PAYLOAD = 11110;
}
