package com.tiger.socol.gu.api.good;

import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class CommentList extends FlowRequest<GoodsCommentEntity> {

    public int goodsId;
    public int page;

    @Override
    public String api() {
        return GoodApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(GoodApi.modules.COMMENT_LIST);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("page", page + "");
        ps.put("goodsId", goodsId + "");
    }

}
