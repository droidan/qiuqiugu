package com.tiger.socol.gu.activity.community.Content;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.CommentEntity;

import java.util.List;

interface CommunityContentView extends MvpView {

    void onSubmitSuccess(CommentEntity commentEntity);

    void onSubmitFailure(String message);

    void onRequestFristPageCommentSuccess(List<CommentEntity> comments, boolean more);

    void onRequestFristPageCommentFailure(String message);

    void onRequestNextPageCommentSuccess(List<CommentEntity> comments, boolean more);

    void onRequestNextPageCommentFailure(String message);

}