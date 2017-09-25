package com.tiger.socol.gu.api.cart;

import com.tiger.socol.gu.network.Module;

public class CartApi {

    public static final String name = "car";

    public static final CartApi.Modules modules = new CartApi().new Modules();

    public class Modules {
        public final Module ADD = new Module("do", 1);
        public final Module LIST = new Module("lists", 1);
        public final Module DELETE = new Module("delete", 1);
    }

}
