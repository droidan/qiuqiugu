package com.tiger.socol.gu.api.index;


import com.tiger.socol.gu.network.Module;

public class IndexApi {
    public static final String name = "index";

    public static final IndexApi.Modules modules = new IndexApi().new Modules();

    public class Modules {
        public final Module LIST = new Module("lists", 1);
        public final Module SLIDE = new Module("slide", 1);
        public final Module SEARCH = new Module("search", 1);
        public final Module KEYWORD = new Module("keyword", 1);
    }

}
