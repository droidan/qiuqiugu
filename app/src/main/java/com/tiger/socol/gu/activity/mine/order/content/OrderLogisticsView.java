package com.tiger.socol.gu.activity.mine.order.content;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.order.ExpressEntity;

interface OrderLogisticsView extends MvpView {

    void onRequestExpressSuccess(ExpressEntity value);

    void onRequestExpressFailure(String message);

}