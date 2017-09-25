package com.tiger.socol.gu.activity.mine.editInfo;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface EditInfoView extends MvpView {

    void onLoadImageSuccess(String avatar);

    void onLoadImageFailure(String message);

    void onEditAvatarSuccess();

    void onEditAvatarFailure(String message);
}