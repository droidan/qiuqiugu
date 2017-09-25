package com.tiger.socol.gu.api.menber;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class ThreeLoginApi extends ObjectRequest<ThreeLoginEntity> {

    public String openId;
    public String nickName;
    public String figureUrl;
    public String platform;

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.SOCIAL);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("openId", openId);
        ps.put("nickName", nickName);
        ps.put("figureUrl", figureUrl);
        ps.put("platform", platform);
        ps.put("gender", "男");
    }

}
