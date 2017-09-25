package com.tiger.socol.gu.api.menber;

import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class EditApi extends ObjectRequest<Member>{

    public String avatar;
    public String password;
    public String nickName;

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.EDIT);
    }

    @Override
    public void parament(Map<String, String> ps) {
        if (!StringUtils.isEmpty(avatar)) {
            ps.put("avatar", avatar);
        }

        if (!StringUtils.isEmpty(password)) {
            ps.put("password", password);
        }

        if (!StringUtils.isEmpty(nickName)) {
            ps.put("nickName", nickName);
        }
    }

}
