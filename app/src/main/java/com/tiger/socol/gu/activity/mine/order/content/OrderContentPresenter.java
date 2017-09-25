package com.tiger.socol.gu.activity.mine.order.content;

import com.blankj.utilcode.utils.ToastUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.order.ConfirmApi;
import com.tiger.socol.gu.api.order.DetailApi;
import com.tiger.socol.gu.api.order.OrderDetailEntity;
import com.tiger.socol.gu.api.order.OrderListEntity;
import com.tiger.socol.gu.network.ObjectRequest;

public class OrderContentPresenter extends MvpBasePresenter<OrderContentView> {

    public void requestDetail(int orderId) {
        DetailApi api = new DetailApi();
        api.orderId = orderId;
        api.request(new ObjectRequest.OnRequestListeren<OrderDetailEntity>() {
            @Override
            public void onSuccess(OrderDetailEntity value) {
                if (getView() == null) return;
                getView().onRequestDetailtSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestDetailtFailure(message);
            }
        });
    }

    public void confirmOrder(int orderId, String sn) {
        ConfirmApi api = new ConfirmApi();
        api.orderId = orderId + "";
        api.sn = sn;
        api.request(new ObjectRequest.OnRequestListeren<MsgEntity>() {
            @Override
            public void onSuccess(MsgEntity value) {
                if (getView() == null) return;
                getView().onConfirmOrderSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onConfirmOrderFailure(message);
            }
        });
    }

}
