package com.tiger.socol.gu.api.bbs;

import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class ADVApi extends ArrayRequest<AdEntity>{
    @Override
    public String api() {
        return BBSApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(BBSApi.modules.ADV);
    }

    @Override
    public void parament(Map<String, String> ps) {

    }

}
