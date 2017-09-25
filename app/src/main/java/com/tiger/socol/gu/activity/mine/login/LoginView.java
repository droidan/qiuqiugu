package com.tiger.socol.gu.activity.mine.login;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.activity.mine.login.bind.PlatInfo;
import com.tiger.socol.gu.api.menber.Member;

interface LoginView extends MvpView {
    void onLoginSuccess(Member member);

    void onLoginFailure(String message);

}