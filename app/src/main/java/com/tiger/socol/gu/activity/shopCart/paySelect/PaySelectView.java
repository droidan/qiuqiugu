package com.tiger.socol.gu.activity.shopCart.paySelect;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tiger.socol.gu.api.order.WechaPayApi;

interface PaySelectView extends MvpView {
    void onRequestSignSuccess(String order);

    void onRequestWechaSignSuccess(PayReq req);

    void onRequestSignFailure(String message);

}