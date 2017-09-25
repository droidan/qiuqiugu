package com.tiger.socol.gu.api.menber;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.NullRequest;

import java.util.List;
import java.util.Map;

public class CheckCodeApi extends NullRequest {

    public String phone;
    public String useAge;
    public String code;

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.CHECK);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("phone", phone);
        ps.put("useAge", useAge);
        ps.put("code", code);
    }

}
