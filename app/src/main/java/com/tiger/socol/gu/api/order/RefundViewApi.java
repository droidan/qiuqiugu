package com.tiger.socol.gu.api.order;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class RefundViewApi extends ObjectRequest<RefundEntity> {

    public int orderId;
    public String sn;

    @Override
    public String api() {
        return OrderApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(OrderApi.modules.REFUNDVIEW);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("orderId", orderId + "");
        ps.put("sn", sn + "");
    }

}
