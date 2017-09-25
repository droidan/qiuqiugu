package com.tiger.socol.gu.api.good;

import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class CollectListApi extends FlowRequest<GoodsEntity> {

    public int page;

    @Override
    public String api() {
        return GoodApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(GoodApi.modules.COLLECT_LIST);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("page", page + "");
    }

}
