package com.tiger.socol.gu.api.good;

import com.tiger.socol.gu.network.Module;

public class GoodApi {

    public static final String name = "goods";

    public static final GoodApi.Modules modules = new GoodApi().new Modules();

    public class Modules {
        public final Module DETAIL = new Module("detail", 1);
        public final Module COLLECT_LIST = new Module("collectlist", 1);
        public final Module COLLECT = new Module("collect", 1);
        public final Module COMMENT_LIST = new Module("commentlist", 1);
    }

}
