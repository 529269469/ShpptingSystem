package com.fhxh.shpptingsystem.ui.adapter.tree;

/**
 * Created by  on 2021/7/10 14:16.
 */
public class HistoryMessage {
    private String Person;
    private String Bureau;
    private String id;

    public HistoryMessage(String person, String bureau, String id) {
        Person = person;
        Bureau = bureau;
        this.id = id;
    }

    public String getPerson() {
        return Person;
    }

    public String getBureau() {
        return Bureau;
    }

    public String getId() {
        return id;
    }
}
