package com.tiger.socol.gu.api.address;


import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class ListApi extends ArrayRequest<AddressEntity> {

    @Override
    public String api() {
        return AddressApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(AddressApi.modules.LIST);
    }

    @Override
    public void parament(Map<String, String> ps) {
    }

}
