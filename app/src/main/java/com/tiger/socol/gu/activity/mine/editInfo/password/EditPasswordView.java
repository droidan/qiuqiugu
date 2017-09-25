package com.tiger.socol.gu.activity.mine.editInfo.password;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface EditPasswordView extends MvpView {

    void onEditPasswordSuccess();

    void onEditPasswordFailure(String message);

}