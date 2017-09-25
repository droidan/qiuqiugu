package com.tiger.socol.gu.api.good;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.NullRequest;

import java.util.List;
import java.util.Map;

public class CollectApi extends NullRequest {

    public boolean collect;
    public int goodsId;

    @Override
    public String api() {
        return GoodApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(GoodApi.modules.COLLECT);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("action", collect ? "collect" : "cancel");
        ps.put("goodsId", goodsId + "");
    }

}
