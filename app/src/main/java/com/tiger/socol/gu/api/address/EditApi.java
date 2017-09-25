package com.tiger.socol.gu.api.address;

import android.location.Address;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class EditApi extends ObjectRequest<AddressEntity> {

    public int addressId;
    public String name;
    public String phone;
    public String province;
    public String city;
    public String area;
    public String address;
    public boolean defaults;

    @Override
    public String api() {
        return AddressApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(AddressApi.modules.EDIT);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("addressId", addressId + "");
        ps.put("name", name);
        ps.put("phone", phone);
        ps.put("address", address);
        ps.put("default", defaults ? "1" : "0");
        ps.put("province", province);
        ps.put("area", area);
        ps.put("city", city);
    }

}
