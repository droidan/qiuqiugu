package com.tiger.socol.gu.activity.mine.find;

import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.menber.CheckCodeApi;
import com.tiger.socol.gu.network.NullRequest;

public class FindPasswordTwoPresenter extends MvpBasePresenter<FindPasswordTwoView> {

    public void validCode(String phone, String code) {
        if (StringUtils.isEmpty(code)) {
            getView().onCheckCodeFailure("请输入验证码");
            return;
        }

        getView().onCheckCodeSuccess();

//        CheckCodeApi api = new CheckCodeApi();
//        api.code = code;
//        api.phone = phone;
//        api.useAge = "findpw";
//        api.request(new NullRequest.OnRequestListeren() {
//            @Override
//            public void onSuccess() {
//                if (getView() == null) return;
//                getView().onCheckCodeSuccess();
//            }
//
//            @Override
//            public void onFailure(String message) {
//                if (getView() == null) return;
//                getView().onCheckCodeFailure(message);
//            }
//        });
    }

}
