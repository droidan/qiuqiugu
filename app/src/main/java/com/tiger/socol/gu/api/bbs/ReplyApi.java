package com.tiger.socol.gu.api.bbs;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class ReplyApi extends ObjectRequest<CommentEntity> {

    public String content;
    public int commentId;

    @Override
    public String api() {
        return BBSApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(BBSApi.modules.REPLY);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("content", content);
        ps.put("commentId", commentId + "");
    }

}
