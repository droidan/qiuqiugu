package com.tiger.socol.gu.api.good;

import com.tiger.socol.gu.api.farm.FarmApi;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class DetailApi extends ObjectRequest<GoodDetailEntity> {

    public int goodsId;
    @Override
    public String api() {
        return GoodApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(GoodApi.modules.DETAIL);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("goodsId", goodsId + "");
    }

}
