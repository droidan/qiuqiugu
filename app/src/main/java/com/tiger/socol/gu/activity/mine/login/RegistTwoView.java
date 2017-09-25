package com.tiger.socol.gu.activity.mine.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface RegistTwoView extends MvpView {

    void onCheckCodeSuccess();

    void onCheckCodeFailure(String message);

}