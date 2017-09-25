package com.tiger.socol.gu.api.bbs;


import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.Method;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;


public class ListApi extends FlowRequest<BBSEntity> {

    public int page;
    public int bbsId;

    @Override
    public Method method() {
        return Method.GET;
    }

    @Override
    public String api() {
        return BBSApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(BBSApi.modules.LIST);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("page", page + "");
        ps.put("bbsId", bbsId + "");
    }

}
