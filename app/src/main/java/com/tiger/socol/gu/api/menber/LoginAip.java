package com.tiger.socol.gu.api.menber;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class LoginAip extends ObjectRequest<Member> {

    public String phone;
    public String password;

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.LOGIN);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("password", password);
        ps.put("phone", phone);
        ps.put("deviceId", "12");
    }

}
