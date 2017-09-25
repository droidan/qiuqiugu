package com.tiger.socol.gu.api.address;


import com.tiger.socol.gu.network.Module;

public class AddressApi {

    public static final String name = "address";

    public static final AddressApi.Modules modules = new AddressApi().new Modules();

    public class Modules {
        public final Module ADD = new Module("add", 1);
        public final Module EDIT = new Module("edit", 1);
        public final Module LIST = new Module("lists", 1);
        public final Module DELETE = new Module("delete", 1);
    }

}
