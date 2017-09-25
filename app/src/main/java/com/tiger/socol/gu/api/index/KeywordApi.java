package com.tiger.socol.gu.api.index;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class KeywordApi extends ObjectRequest<KeywordData> {

    @Override
    public String api() {
        return IndexApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(IndexApi.modules.KEYWORD);
    }

    @Override
    public void parament(Map<String, String> ps) {

    }

}
