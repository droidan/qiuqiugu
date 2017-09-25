package com.tiger.socol.gu.api.bbs;


import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class CommentListApi extends FlowRequest<CommentEntity> {

    public int bbsId;
    public int page;

    @Override
    public String api() {
        return BBSApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(BBSApi.modules.COMMENT_LIST);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("bbsId", bbsId+ "");
        ps.put("page", page + "");
    }

}
