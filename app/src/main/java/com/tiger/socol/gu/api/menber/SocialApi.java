package com.tiger.socol.gu.api.menber;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class SocialApi extends ObjectRequest<SocialEntity> {
    /**
     * 平台昵称
     */
    public String nickName;

    /**
     * 平台唯一id
     */
    public String openId;

    /**
     * 平台头像地址
     */
    public String figureUrl;

    /**
     * 平台名称（qq, weixin, sina）
     */
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
        ps.put("platform", platform);
        ps.put("nickName", nickName);
        ps.put("figureUrl", figureUrl);
    }

}
