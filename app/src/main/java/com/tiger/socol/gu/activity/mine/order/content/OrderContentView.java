package com.tiger.socol.gu.activity.mine.order.content;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.order.OrderDetailEntity;

interface OrderContentView extends MvpView {

    void onRequestDetailtSuccess(OrderDetailEntity detailEntity);

    void onRequestDetailtFailure(String message);

    void onConfirmOrderSuccess();

    void onConfirmOrderFailure(String message);

}