package com.tiger.socol.gu.activity.mine.login;

import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.menber.CheckCodeApi;
import com.tiger.socol.gu.network.NullRequest;

public class RegistTwoPresenter extends MvpBasePresenter<RegistTwoView> {

    public boolean codeIsEmpty(String code) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        return true;
    }

    public void validCode(String phone, String code) {
        CheckCodeApi api = new CheckCodeApi();
        api.code = code;
        api.phone = phone;
        api.useAge = "regist";
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                if (getView() == null) return;
                getView().onCheckCodeSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onCheckCodeFailure(message);
            }
        });
    }

}
