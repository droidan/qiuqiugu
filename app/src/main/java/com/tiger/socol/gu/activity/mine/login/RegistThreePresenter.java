package com.tiger.socol.gu.activity.mine.login;

import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class RegistThreePresenter extends MvpBasePresenter<RegistThreeView> {

    public String checkPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return "密码不能为空";
        }

        if (password.length() < 6) {
            return "密码不能小于6位";
        }

        return null;
    }

}
