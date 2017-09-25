package com.tiger.socol.gu.api.order;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class WechaPayApi extends ObjectRequest<WechaPayApi.WechaSign> {
    public String sn;

    @Override
    public String api() {
        return OrderApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(OrderApi.modules.SIGN);
    }

    @Override
    public void parament(Map<String, String> ps) {
        ps.put("sn", sn);
        ps.put("payType", "wechat");
    }

    public class WechaSign {
        private String type;
        private WechaSginEntity sign;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public WechaSginEntity getSign() {
            return sign;
        }

        public void setSign(WechaSginEntity sign) {
            this.sign = sign;
        }
    }
}
