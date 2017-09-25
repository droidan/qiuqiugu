package com.tiger.socol.gu.api.farm;

import com.tiger.socol.gu.network.Module;

public class FarmApi {

    public static final String name = "base";

    public static final FarmApi.Modules modules = new FarmApi().new Modules();

    public class Modules {
        public final Module DETAIL = new Module("detail", 1);
    }

}
