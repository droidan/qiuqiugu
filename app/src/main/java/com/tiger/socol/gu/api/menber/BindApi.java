package com.tiger.socol.gu.api.menber;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class BindApi extends ObjectRequest<Member> {

    public String code;
    public String phone;
    public String openId;

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.BIND);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("code", code);
        ps.put("phone", phone);
        ps.put("openId", openId);
    }

}
