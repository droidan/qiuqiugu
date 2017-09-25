package com.tiger.socol.gu.api.good;

import com.tiger.socol.gu.api.order.OrderApi;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.NullRequest;

import java.util.List;
import java.util.Map;

public class CommentApi extends NullRequest {

    // 订单号
    public int orderId;
    // 物流星级
    public float start;
    // 评论主体
    public String comments;
    // 是否匿名
    public boolean isAnonymous;

    @Override
    public String api() {
        return OrderApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(OrderApi.modules.COMMENT);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("comments", comments);
        ps.put("orderId", orderId + "");
        ps.put("expressStars", start + "");
        ps.put("isAnonymous", isAnonymous ? "yes" : "no");
    }

}
