package com.tiger.socol.gu.api.farm;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class DetailApi extends ObjectRequest<FarmDetailEntity> {

    public int baseId;

    @Override
    public String api() {
        return FarmApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(FarmApi.modules.DETAIL);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("baseId", baseId + "");
    }

}
