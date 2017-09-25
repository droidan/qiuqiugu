package com.tiger.socol.gu.api.menber;


import com.tiger.socol.gu.network.Method;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.NullRequest;

import java.util.List;
import java.util.Map;

public class LogoutApi extends NullRequest {

    @Override
    public Method method() {
        return Method.GET;
    }

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.LOGOUT);
    }

    @Override
    public void parament(Map<String, String> ps) {
    }

}
