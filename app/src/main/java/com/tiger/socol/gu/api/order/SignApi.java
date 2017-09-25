package com.tiger.socol.gu.api.order;

import com.tiger.socol.gu.network.Module;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;
import java.util.Map;

public class SignApi extends ObjectRequest<SignApi.OrderSign> {

    public String sn;
    public String type;

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
        ps.put("payType", type);
    }

    public class OrderSign {
        private String type;
        private String sign;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
