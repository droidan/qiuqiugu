package com.tiger.socol.gu.activity.mine.find;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface FindPasswordOneView extends MvpView {

    void onSendSMSCodeSuccess(String phone);

    void onSendSMSCodeFailure(String message);

}