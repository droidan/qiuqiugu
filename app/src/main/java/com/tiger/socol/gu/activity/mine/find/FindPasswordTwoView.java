package com.tiger.socol.gu.activity.mine.find;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface FindPasswordTwoView extends MvpView {
    void onCheckCodeSuccess();

    void onCheckCodeFailure(String message);

}