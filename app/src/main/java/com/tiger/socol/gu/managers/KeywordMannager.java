package com.tiger.socol.gu.managers;

import com.tiger.socol.gu.utils.RealmMannager;

import java.util.ArrayList;
import java.util.List;


public class KeywordMannager {

    private static KeywordMannager single = null;
    private List<String> list = new ArrayList<>();

    private KeywordMannager() {
    }

    public static KeywordMannager getInstance() {
        if (single == null) {
            synchronized (KeywordMannager.class) {
                if (single == null) {
                    single = new KeywordMannager();
                }
            }
        }
        return single;
    }

    public List<String> list() {
        list.clear();
        List<KeywordEntity> ks = RealmMannager.getInstance().syncFindAll(KeywordEntity.class);
        for (KeywordEntity k : ks) {
            list.add(k.getName());
        }
        return list;
    }

    public void add(String keyword) {
        if (list.contains(keyword)) {
            return;
        }
        list.add(keyword);
        KeywordEntity entity = new KeywordEntity();
        entity.setName(keyword);
        RealmMannager.getInstance().asyncSava(entity);
    }

    public void delete(String keyword) {
        list.remove(keyword);
        KeywordEntity entity = new KeywordEntity();
        entity.setName(keyword);
        RealmMannager.getInstance().asyncDelete(entity);
    }

    public void clean() {
        list.clear();
        RealmMannager.getInstance().asyncDeleteAll(KeywordEntity.class);
    }

}
