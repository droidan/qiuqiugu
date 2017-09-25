package com.tiger.socol.gu.api.menber;

import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class RebindApi extends ObjectRequest<MsgEntity> {
    public String code;
    public String phone;

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.REBIND);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("code", code);
        ps.put("phone", phone);
    }
}
