package com.tiger.socol.gu.api.bbs;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.NullRequest;

import java.util.List;
import java.util.Map;

public class LikeApi extends NullRequest {

    public int bbsId;

    @Override
    public String api() {
        return BBSApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(BBSApi.modules.LIKES);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("bbsId", bbsId + "");
        ps.put("action", "digg");
    }

}
