package com.tiger.socol.gu.activity.mine.order.content;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.order.RefundEntity;

interface RefundView extends MvpView {

    void onRequestRefundSuccess(RefundEntity refund);

    void onRequestRefundFailure(String message);

}