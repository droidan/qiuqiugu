package com.tiger.socol.gu.activity.product.assess;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.good.CommentApi;
import com.tiger.socol.gu.network.NullRequest;

public class AssessPresenter extends MvpBasePresenter<AssessView> {

    public void submit(int orderId, float start, String comments, boolean isAnonymous) {
        CommentApi api = new CommentApi();
        api.start = start;
        api.orderId = orderId;
        api.comments = comments;
        api.isAnonymous = isAnonymous;
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                if (getView() == null) return;
                getView().onCommentSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onCommentFailure(message);
            }
        });
    }

}
