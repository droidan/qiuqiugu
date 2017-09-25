package com.tiger.socol.gu.api.menber;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.NullRequest;

import java.util.List;
import java.util.Map;

public class SendSMSCode extends NullRequest {

    public String phone;
    // regist 用户注册
    // bind	绑定第三方账号
    // resetPassword 重置密码	并且用户是否为限制登录状态
    public String useAge;

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.SMS);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("phone", phone);
        ps.put("useAge", useAge);
    }

}
