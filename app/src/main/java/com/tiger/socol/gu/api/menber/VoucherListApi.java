package com.tiger.socol.gu.api.menber;

import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.Module;

import java.util.List;
import java.util.Map;

public class VoucherListApi extends ArrayRequest<VoucherEntity> {

    @Override
    public String api() {
        return MemberApi.name;
    }

    @Override
    public void modules(List<Module> ms) {
        ms.add(MemberApi.modules.VOUCHER);
    }

    @Override
    public void parament(Map<String, String> ps) {

    }

}
