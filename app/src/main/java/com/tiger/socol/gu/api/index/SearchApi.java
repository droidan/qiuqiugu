package com.tiger.socol.gu.api.index;

import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;


public class SearchApi extends FlowRequest<GoodsEntity> {

    public String keyword;

    @Override
    public String api() {
        return IndexApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(IndexApi.modules.SEARCH);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("keyword", keyword);
    }

}
