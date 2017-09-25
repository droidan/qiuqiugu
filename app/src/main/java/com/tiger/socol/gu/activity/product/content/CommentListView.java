package com.tiger.socol.gu.activity.product.content;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.good.GoodsCommentEntity;

import java.util.List;

interface CommentListView extends MvpView {

    void onRequestFristPageSuccess(List<GoodsCommentEntity> comments, boolean more);

    void onRequestNextPageSuccess(List<GoodsCommentEntity> comments, boolean more);

    void onRequestNextPageFailure(String message);

    void onRequestFristPageFailure(String message);

}