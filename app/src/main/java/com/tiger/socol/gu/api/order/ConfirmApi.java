package com.tiger.socol.gu.api.order;

import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class ConfirmApi extends ObjectRequest<MsgEntity> {

    public String orderId;
    public String sn;

    @Override
    public String api() {
        return OrderApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(OrderApi.modules.CONFIRM);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("sn", sn);
        ps.put("orderId", orderId );
    }

}
