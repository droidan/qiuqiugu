package com.tiger.socol.gu.api.bbs;

import com.tiger.socol.gu.network.GsonUtil;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.io.File;
import java.util.List;
import java.util.Map;

public class AddApi extends ObjectRequest<CommentEntity> {

    public String contexnt;
    public List<String> attachment;

    @Override
    public String api() {
        return BBSApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(BBSApi.modules.POST);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("content", contexnt);
        if (null != attachment && attachment.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (String s : attachment) {
                sb.append(s);
                sb.append(",");
            }
            String s = sb.toString().substring(0, sb.length() - 1);
            ps.put("attachment", s);
        }
    }

    @Override
    public void files(Map<String, File> fs) {

    }

}
