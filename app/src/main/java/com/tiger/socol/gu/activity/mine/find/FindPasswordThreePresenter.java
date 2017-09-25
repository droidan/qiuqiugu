package com.tiger.socol.gu.activity.mine.find;

import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.menber.FindApi;
import com.tiger.socol.gu.network.NullRequest;

public class FindPasswordThreePresenter extends MvpBasePresenter<FindPasswordThreeView> {

    public void find(String phone, String code, String password) {

        if (StringUtils.isEmpty(password)) {
            getView().onFindPasswordFailure("请输入密码");
            return;
        }
        if (password.length() < 6) {
            getView().onFindPasswordFailure("密码不能少于6位");
            return;
        }

        FindApi api = new FindApi();
        api.phone = phone;
        api.code = code;
        api.password = password;
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                if (getView() == null) return;
                getView().onFindPasswordSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onFindPasswordFailure(message);
            }
        });
    }

}
