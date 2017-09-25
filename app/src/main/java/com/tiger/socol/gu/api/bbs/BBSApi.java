package com.tiger.socol.gu.api.bbs;

import com.tiger.socol.gu.network.Module;

public class BBSApi {

    public static final String name = "bbs";

    public static final BBSApi.Modules modules = new BBSApi().new Modules();

    public class Modules {
        public final Module ADV = new Module("adv", 1);
        public final Module POST = new Module("post", 1);
        public final Module LIKES = new Module("likes", 1);
        public final Module REPLY = new Module("reply", 1);
        public final Module LIST = new Module("bbslist", 1);
        public final Module COMMENT = new Module("comment", 1);
        public final Module COMMENT_LIST = new Module("commentlist", 1);
    }

}
