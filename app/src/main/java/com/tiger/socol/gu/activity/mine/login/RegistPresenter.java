package com.tiger.socol.gu.activity.mine.login;

import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.menber.SendSMSCode;
import com.tiger.socol.gu.network.NullRequest;

public class RegistPresenter extends MvpBasePresenter<RegistView> {

    public boolean checkPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return true;
    }

    public void sendSMSCode(final String phone) {
        SendSMSCode api = new SendSMSCode();
        api.phone = phone;
        api.useAge = "regist";
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
