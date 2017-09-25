package com.tiger.socol.gu.activity.mine.login.bind;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.menber.Member;

interface BindView extends MvpView {
    void onBindSuccess();

    void onBindFailure(String message);

    void onSendSMSStatus(String message);

}