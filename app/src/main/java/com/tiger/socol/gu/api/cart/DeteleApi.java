package com.tiger.socol.gu.api.cart;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class DeteleApi extends ObjectRequest<MsgEntity> {

    public String goodsId;

    @Override
    public String api() {
        return CartApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(CartApi.modules.DELETE);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("goodsId", goodsId);
    }
}
