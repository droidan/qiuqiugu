package com.tiger.socol.gu.activity.mine.find;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface FindPasswordThreeView extends MvpView {

    void onFindPasswordSuccess();

    void onFindPasswordFailure(String message);

}