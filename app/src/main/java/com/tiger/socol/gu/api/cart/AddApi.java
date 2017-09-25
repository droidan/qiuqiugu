package com.tiger.socol.gu.api.cart;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class AddApi extends ObjectRequest<MsgEntity> {

    public int goodsId;

    @Override
    public String api() {
        return CartApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(CartApi.modules.ADD);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("goodsId", goodsId + "");
        ps.put("action", "add");
    }

}
