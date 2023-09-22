package com.fhxh.shpptingsystem.ui.adapter;


import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.fhxh.shpptingsystem.ui.adapter.provider.FifthProvider;
import com.fhxh.shpptingsystem.ui.adapter.provider.FirstProvider;
import com.fhxh.shpptingsystem.ui.adapter.provider.FourthProvider;
import com.fhxh.shpptingsystem.ui.adapter.provider.SecondProvider;
import com.fhxh.shpptingsystem.ui.adapter.provider.ThirdProvider;
import com.fhxh.shpptingsystem.ui.adapter.tree.FifthNode;
import com.fhxh.shpptingsystem.ui.adapter.tree.FirstNode;
import com.fhxh.shpptingsystem.ui.adapter.tree.FourthNode;
import com.fhxh.shpptingsystem.ui.adapter.tree.SecondNode;
import com.fhxh.shpptingsystem.ui.adapter.tree.ThirdNode;


import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by  on 2021/6/19.
 * 打靶日期 记录 树状列表
 */

public class HistoryDataAdapter extends BaseNodeAdapter {
    public HistoryDataAdapter() {
        addNodeProvider(new FirstProvider());
        addNodeProvider(new SecondProvider());
        addNodeProvider(new ThirdProvider());
        addNodeProvider(new FourthProvider());
        addNodeProvider(new FifthProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> list, int position) {
        BaseNode node = list.get(position);
        if (node instanceof FirstNode) {
            return 1;
        } else if (node instanceof SecondNode) {
            return 2;
        } else if (node instanceof ThirdNode) {
            return 3;
        } else if (node instanceof FourthNode){
            return 4;
        } else if (node instanceof FifthNode){
            return 5;
        }
        return -1;
    }

    public static final int EXPAND_COLLAPSE_PAYLOAD = 11110;
}
