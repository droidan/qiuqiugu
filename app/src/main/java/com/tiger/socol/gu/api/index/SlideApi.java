package com.tiger.socol.gu.api.index;

import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class SlideApi extends ArrayRequest<FarmEntity> {

    @Override
    public String api() {
        return IndexApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(IndexApi.modules.SLIDE);
    }

    @Override
    public void parament(Map<String, String> ps) {

    }
}
