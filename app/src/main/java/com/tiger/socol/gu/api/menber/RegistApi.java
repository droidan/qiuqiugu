package com.tiger.socol.gu.api.menber;


import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.io.File;
import java.util.List;
import java.util.Map;

public class RegistApi extends ObjectRequest<Member> {

    public String phone;
    public String avatar;
    public String password;
    public String nickName;

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.REGIST);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("phone", phone);
        ps.put("password", password);
        ps.put("nickName", nickName);
        ps.put("avatar", "");
    }

    @Override
    public void files(Map<String, File> fs) {
//        fs.put("avatar", avatar);
    }

}
