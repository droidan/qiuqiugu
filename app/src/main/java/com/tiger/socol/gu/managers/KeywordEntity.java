package com.tiger.socol.gu.managers;

import io.realm.RealmObject;

public class KeywordEntity extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeywordEntity(String name) {
        this.name = name;
    }

    public KeywordEntity() {
    }
}