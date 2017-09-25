package com.tiger.socol.gu.api.address;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class AddApi extends ObjectRequest<AddressEntity> {

    public String name;
    public String phone;
    public String userId;
    public String address;
    public String province;
    public String city;
    public String area;
    //    0不默认，1为默认
    public Boolean defaults;

    @Override
    public String api() {
        return AddressApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(AddressApi.modules.ADD);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("name", name);
        ps.put("phone", phone);
        ps.put("userId", userId);
        ps.put("address", address);
        ps.put("province", province);
        ps.put("city", city);
        ps.put("area", area);
        ps.put("default", defaults ? "1" : "0");
    }

}
