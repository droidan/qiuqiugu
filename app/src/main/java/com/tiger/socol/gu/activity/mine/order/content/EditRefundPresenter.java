package com.tiger.socol.gu.activity.mine.order.content;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.order.RefundApi;
import com.tiger.socol.gu.network.ObjectRequest;

public class EditRefundPresenter extends MvpBasePresenter<EditRefundView> {

    public void refund(int orderId, String sn, String casex) {
        RefundApi api = new RefundApi();
        api.orderId = orderId;
        api.sn = sn;
        api.casex = casex;
        api.request(new ObjectRequest.OnRequestListeren<MsgEntity>() {
            @Override
            public void onSuccess(MsgEntity value) {
                if (getView() == null) return;
                getView().onRefundSuccess(value.getMsg());
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRefundFailure(message);
            }
        });

    }
}
