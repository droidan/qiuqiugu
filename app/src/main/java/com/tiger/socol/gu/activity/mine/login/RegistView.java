package com.tiger.socol.gu.activity.mine.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface RegistView extends MvpView {

    void onSendSMSCodeSuccess(String phone);

    void onSendSMSCodeFailure(String message);

}