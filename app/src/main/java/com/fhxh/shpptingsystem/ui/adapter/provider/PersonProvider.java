package com.fhxh.shpptingsystem.ui.adapter.provider;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fhxh.shpptingsystem.R;
import com.fhxh.shpptingsystem.ui.adapter.HistoryPersonAdapter;
import com.fhxh.shpptingsystem.ui.adapter.tree.PersonNode;


import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by  on 2021/7/10 14:40.
 */
public class PersonProvider extends BaseNodeProvider {
    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_node_person;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        PersonNode entity = (PersonNode) data;
        helper.setText(R.id.title, entity.getTitle());
        helper.setImageResource(R.id.iv, R.mipmap.arrow_r);

        setArrowSpin(helper, data, false);
    }
    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data, @NotNull List<?> payloads) {
        for (Object payload : payloads) {
            if (payload instanceof Integer && (int) payload == HistoryPersonAdapter.EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                setArrowSpin(helper, data, true);
            }
        }
    }

    private void setArrowSpin(BaseViewHolder helper, BaseNode data, boolean isAnimate) {
        PersonNode entity = (PersonNode) data;

        ImageView imageView = helper.getView(R.id.iv);

        if (entity.isExpanded()) {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .rotation(90f)
                        .start();
            } else {
                imageView.setRotation(90f);
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .rotation(0f)
                        .start();
            } else {
                imageView.setRotation(0f);
            }
        }
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter().expandOrCollapse(position, true, true, HistoryPersonAdapter.EXPAND_COLLAPSE_PAYLOAD);
    }

    public int SelectData(String personname){
        int index = -1;
        List<BaseNode> list = getAdapter().getData();
        for (int i = 0; i < list.size(); i++) {
            PersonNode personNode = (PersonNode)list.get(i);
            if(personname.equals(personNode.getTitle())){
                index = i;
            }
        }
        if(index != -1){
            getAdapter().expandAndCollapseOther(index, true, true, true, true, HistoryPersonAdapter.EXPAND_COLLAPSE_PAYLOAD);
        }
        return index;
    }

}
