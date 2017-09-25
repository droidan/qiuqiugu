package com.tiger.socol.gu.activity.mine.order;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.order.OrderListEntity;

import java.util.List;

interface OrderTypeView extends MvpView {
    void onRequestFistPageSuccess(List<OrderListEntity> orders, boolean more);

    void onRequestNextPageSuccess(List<OrderListEntity> orders, boolean more);

    void onRequestFistPageFailure(String message);

    void onRequestNextPageFailure(String message);
}