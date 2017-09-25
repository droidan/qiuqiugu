package com.tiger.socol.gu.activity.mine.login;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.menber.Member;

interface PerfectInfoView extends MvpView {

    void onRegistSuccess(Member member);

    void onRegistFailure(String message);

    void onLoadImageSuccess(String avatar);

    void onLoadImageFailure(String message);

}