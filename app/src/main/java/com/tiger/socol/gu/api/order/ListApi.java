package com.tiger.socol.gu.api.order;


import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class ListApi extends FlowRequest<OrderListEntity> {

    public int page;
    // Unpaid 未付款
    // Pendding 已付款、未发货
    // Dispatch 已发货
    // Complete 完成
    // Returns 换货
    // Refund 退货
    public String orderStatus;

    @Override
    public String api() {
        return OrderApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(OrderApi.modules.LIST);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("page", page + "");
        ps.put("orderStatus", orderStatus);
    }

}
