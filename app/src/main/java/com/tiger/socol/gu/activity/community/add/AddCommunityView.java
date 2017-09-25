package com.tiger.socol.gu.activity.community.add;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.api.upload.UploadImageEntity;

import java.util.ArrayList;

interface AddCommunityView extends MvpView {

    void onLoadImageSuccess(ArrayList<String> images);

    void onLoadImageFailure(String message);

    void onAddSuccess(CommentEntity value);

    void onAddFailure(String message);

}