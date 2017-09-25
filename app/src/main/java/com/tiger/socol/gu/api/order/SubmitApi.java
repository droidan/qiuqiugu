package com.tiger.socol.gu.api.order;

import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class SubmitApi extends ObjectRequest<OrderEntity> {

    public String goods;
    public int addressId;
    public String payMode;
    public String voucher;
    public double totalPrice;
    public double goodsPrice;
    public double expressPrice;

    @Override
    public String api() {
        return OrderApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(OrderApi.modules.SUBMIT);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("goods", goods);
        ps.put("payMode", payMode);
        if (!StringUtils.isEmpty(voucher)) {
            ps.put("voucher", voucher);
        }
        ps.put("addressId", addressId + "");
        ps.put("totalPrice", totalPrice + "");
        ps.put("goodsPrice", goodsPrice + "");
        ps.put("expressPrice", expressPrice + "");

    }

}
