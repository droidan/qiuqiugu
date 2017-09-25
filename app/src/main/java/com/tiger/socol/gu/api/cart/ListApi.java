package com.tiger.socol.gu.api.cart;

import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class ListApi extends ArrayRequest<GoodsEntity> {

    @Override
    public String api() {
        return CartApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(CartApi.modules.LIST);
    }

    @Override
    public void parament(Map<String, String> ps) {

    }

}
