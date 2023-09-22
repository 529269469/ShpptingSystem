package com.fhxh.shpptingsystem.ui.adapter.tree;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FifthNode extends BaseExpandNode {
    private String title;
    private String person;
    private String id;

    public FifthNode(String title, String person, String id) {
        this.title = title;
        this.person = person;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getPerson() {
        return person;
    }

    public String getId() {
        return id;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }


}
