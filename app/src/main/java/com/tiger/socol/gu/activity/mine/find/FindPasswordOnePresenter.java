package com.tiger.socol.gu.activity.mine.find;

import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.menber.SendSMSCode;
import com.tiger.socol.gu.network.NullRequest;

public class FindPasswordOnePresenter extends MvpBasePresenter<FindPasswordOneView> {

    public void sendSMSCode(final String phone) {
        if (StringUtils.isEmpty(phone)) {
            getView().onSendSMSCodeFailure("请输入手机号");
            return;
        }

        SendSMSCode api = new SendSMSCode();
        api.phone = phone;
        api.useAge = "findpw";
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                if (getView() == null) return;
                getView().onSendSMSCodeSuccess(phone);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onSendSMSCodeFailure(message);
            }
        });
    }

}
