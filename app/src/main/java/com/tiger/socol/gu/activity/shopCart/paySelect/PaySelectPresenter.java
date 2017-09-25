package com.tiger.socol.gu.activity.shopCart.paySelect;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tiger.socol.gu.api.order.SignApi;
import com.tiger.socol.gu.api.order.WechaPayApi;
import com.tiger.socol.gu.network.ObjectRequest;

public class PaySelectPresenter extends MvpBasePresenter<PaySelectView> {

    private Context context;

    public PaySelectPresenter(Context context) {
        this.context = context;
    }

    public void requestSgin(String sn, String type) {
        SignApi api = new SignApi();
        api.sn = sn;
        api.type = type;
        api.request(new ObjectRequest.OnRequestListeren<SignApi.OrderSign>() {
            @Override
            public void onSuccess(SignApi.OrderSign value) {
                if (getView() == null) return;
                getView().onRequestSignSuccess(value.getSign());
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestSignFailure(message);
            }
        });
    }

    public void wechaPay(String sn) {
        WechaPayApi api = new WechaPayApi();
        api.sn = sn;
        api.request(new ObjectRequest.OnRequestListeren<WechaPayApi.WechaSign>() {
            @Override
            public void onSuccess(WechaPayApi.WechaSign value) {
                if (getView() == null) return;

                PayReq req = new PayReq();
                req.sign = value.getSign().getSign();
                req.appId = value.getSign().getAppid();
                req.nonceStr = value.getSign().getNoncestr();
                req.prepayId = value.getSign().getPrepayid();
                req.timeStamp = value.getSign().getTimestamp();
                req.partnerId = value.getSign().getPartnerid();
                req.packageValue = value.getSign().getPackagevalue();
                getView().onRequestWechaSignSuccess(req);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestSignFailure(message);
            }
        });
    }

}
