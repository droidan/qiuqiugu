package com.tiger.socol.gu.activity.mine.order.content;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.order.RefundEntity;
import com.tiger.socol.gu.api.order.RefundViewApi;
import com.tiger.socol.gu.network.ObjectRequest;

public class RefundPresenter extends MvpBasePresenter<RefundView> {

    public void refund(int orderId, String sn) {
        RefundViewApi api = new RefundViewApi();
        api.orderId = orderId;
        api.sn = sn;
        api.request(new ObjectRequest.OnRequestListeren<RefundEntity>() {
            @Override
            public void onSuccess(RefundEntity value) {
                if (getView() == null) return;
                getView().onRequestRefundSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestRefundFailure(message);
            }
        });
    }

}
