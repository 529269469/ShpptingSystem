package com.fhxh.shpptingsystem.ui.adapter.tree;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FourthNode extends BaseExpandNode {
    private List<BaseNode> childNode;
    private String title;

    public FourthNode(List<BaseNode> childNode, String title) {
        this.childNode = childNode;
        this.title = title;
        setExpanded(false);
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return childNode;
    }
}
