package com.tiger.socol.gu.api.menber;


import com.tiger.socol.gu.network.Module;

public class MemberApi {

    public static final String name = "member";

    public static final Modules modules = new MemberApi().new Modules();

    public class Modules {
        /**
         * 第三方账号绑定手机号
         */
        public final Module BIND = new Module("bind", 1);

        /**
         * 校验验证码
         */
        public final Module CHECK = new Module("check", 1);

        /**
         * 编辑用户资料
         */
        public final Module EDIT = new Module("edit", 1);

        /**
         * 登录
         */
        public final Module LOGIN = new Module("login", 1);

        /**
         * 注册
         */
        public final Module REGIST = new Module("regist", 1);

        /**
         * 退出登录
         */
        public final Module LOGOUT = new Module("logout", 1);

        /**
         * 发送验证码
         */
        public final Module SMS = new Module("sms", 1);

        /**
         * 第三方用户登录
         */
        public final Module SOCIAL = new Module("social", 1);
        public final Module VOUCHER = new Module("voucher", 1);
        public final Module RESEPW = new Module("resetpw", 1);
        public final Module FINDPW = new Module("findpw", 1);
        public final Module REBIND = new Module("rebind", 1);
    }

}
