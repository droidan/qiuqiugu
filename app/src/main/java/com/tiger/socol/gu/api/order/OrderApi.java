package com.tiger.socol.gu.api.order;


import com.tiger.socol.gu.network.Module;

public class OrderApi {

    public static final String name = "order";

    public static final OrderApi.Modules modules = new OrderApi().new Modules();

    public class Modules {
        public final Module SUBMIT = new Module("add", 1);
        public final Module LIST = new Module("lists", 1);
        public final Module DETAIL = new Module("detail", 1);
        public final Module EXPRESS = new Module("express", 1);
        public final Module POSTTAGE = new Module("postage", 1);
        public final Module REFUNDVIEW = new Module("refundview", 1);
        public final Module REFUND = new Module("refundapply", 1);
        public final Module CONFIRM = new Module("confirm", 1);
        public final Module SIGN = new Module("sign", 1);
        public final Module COMMENT = new Module("comment", 1);
        public final Module NUM = new Module("num", 1);
    }

}
