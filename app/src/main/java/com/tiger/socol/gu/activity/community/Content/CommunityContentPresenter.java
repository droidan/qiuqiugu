package com.tiger.socol.gu.activity.community.Content;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.bbs.AddApi;
import com.tiger.socol.gu.api.bbs.AddCommentApi;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.api.bbs.CommentListApi;
import com.tiger.socol.gu.api.bbs.ListApi;
import com.tiger.socol.gu.api.bbs.ReplyApi;
import com.tiger.socol.gu.network.FlowEntity;
import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.ArrayList;
import java.util.List;

public class CommunityContentPresenter extends MvpBasePresenter<CommunityContentView> {

    private int page = 0;
    private List<CommentEntity> comments = new ArrayList<>();

    public void submit(String content, int bbsId) {
        AddCommentApi api = new AddCommentApi();
        api.bbsId = bbsId;
        api.text = content;
        api.request(new ObjectRequest.OnRequestListeren<CommentEntity>() {
            @Override
            public void onSuccess(CommentEntity value) {
                if (getView() == null) return;
                getView().onSubmitSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onSubmitFailure(message);
            }
        });
    }

    public void reply(int commentId, String content) {
        ReplyApi api = new ReplyApi();
        api.commentId = commentId;
        api.content = content;
        api.request(new ObjectRequest.OnRequestListeren<CommentEntity>() {
            @Override
            public void onSuccess(CommentEntity value) {
                if (getView() == null) return;
                getView().onSubmitSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onSubmitFailure(message);
            }
        });
    }

    public void commentList(int bbsId) {
        final CommentListApi api = new CommentListApi();
        api.page = page;
        api.bbsId = bbsId;
        api.request(new FlowRequest.OnRequestListeren<CommentEntity>() {
            @Override
            public void onSuccess(FlowEntity<CommentEntity> value) {
                if (getView() == null) return;
                if (page == 0) {
                    comments.clear();
                    comments.addAll(value.getData());
                    getView().onRequestFristPageCommentSuccess(comments, value.isNextPage());
                } else {
                    comments.addAll(value.getData());
                    getView().onRequestNextPageCommentSuccess(comments, value.isNextPage());
                }
                page++;
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                if (page == 0) {
                    getView().onRequestFristPageCommentFailure(message);
                } else {
                    getView().onRequestNextPageCommentFailure(message);
                }
            }
        });
    }

    public void requestFristPageComment(int bbsId) {
        page = 0;
        commentList(bbsId);
    }

    public void requestNextPageComment(int bbsId) {
        commentList(bbsId);
    }

}
