package com.tiger.socol.gu.api.order;

import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class RefundApi extends ObjectRequest<MsgEntity> {

    public String sn;
    public int orderId;
    public String casex;

    @Override
    public String api() {
        return OrderApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(OrderApi.modules.REFUND);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("sn", sn);
        ps.put("cause", casex);
        ps.put("orderId", orderId + "");
    }

}
