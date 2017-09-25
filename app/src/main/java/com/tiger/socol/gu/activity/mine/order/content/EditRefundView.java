package com.tiger.socol.gu.activity.mine.order.content;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface EditRefundView extends MvpView {
    void onRefundSuccess(String message);

    void onRefundFailure(String message);
}