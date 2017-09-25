package com.tiger.socol.gu.activity.mine.editInfo.name;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface EditNameView extends MvpView {

    void onEditNameSuccess();

    void onEditNameFailure(String message);
}