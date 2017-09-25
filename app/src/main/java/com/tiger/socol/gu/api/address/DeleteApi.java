package com.tiger.socol.gu.api.address;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.NullRequest;

import java.util.List;
import java.util.Map;

public class DeleteApi extends NullRequest {

    public String addressId;

    @Override
    public String api() {
        return AddressApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(AddressApi.modules.DELETE);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("addressId", addressId);
    }

}
