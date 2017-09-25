package com.tiger.socol.gu.activity.product.assess;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface AssessView extends MvpView {

    void onCommentSuccess();

    void onCommentFailure(String message);
}