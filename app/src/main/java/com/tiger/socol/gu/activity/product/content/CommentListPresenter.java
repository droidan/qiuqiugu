package com.tiger.socol.gu.activity.product.content;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.good.CommentList;
import com.tiger.socol.gu.api.good.GoodsCommentEntity;
import com.tiger.socol.gu.network.FlowEntity;
import com.tiger.socol.gu.network.FlowRequest;

import java.util.ArrayList;
import java.util.List;

public class CommentListPresenter extends MvpBasePresenter<CommentListView> {

    private int page = 0;

    private List<GoodsCommentEntity> comments = new ArrayList<>();

    private void requestCommentList(int goodId) {
        CommentList api = new CommentList();
        api.goodsId = goodId;
        api.page = page;
        api.request(new FlowRequest.OnRequestListeren<GoodsCommentEntity>() {
            @Override
            public void onSuccess(FlowEntity<GoodsCommentEntity> value) {
                if (getView() == null) return;
                if (page == 0) {
                    comments.clear();
                    comments.addAll(value.getData());
                    getView().onRequestFristPageSuccess(comments, value.isNextPage());
                } else {
                    comments.addAll(value.getData());
                    getView().onRequestNextPageSuccess(comments, value.isNextPage());
                }
                page++;
            }

            @Override
            public void onFailure(String message) {
                if (page == 0) {
                    getView().onRequestFristPageFailure(message);
                } else {
                    getView().onRequestNextPageFailure(message);
                }
            }
        });
    }

    public void requestFristPage(int goodId) {
        page = 0;
        requestCommentList(goodId);
    }

    public void requestNextPage(int goodId) {
        requestCommentList(goodId);
    }

}
