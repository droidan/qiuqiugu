package com.tiger.socol.gu.api.bbs;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class AddCommentApi extends ObjectRequest<CommentEntity> {

    public int bbsId;
    public String text;

    @Override
    public String api() {
        return BBSApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(BBSApi.modules.COMMENT);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("bbsId", bbsId + "");
        ps.put("text", text);
    }

}
