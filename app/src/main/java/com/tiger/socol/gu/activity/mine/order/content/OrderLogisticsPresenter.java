package com.tiger.socol.gu.activity.mine.order.content;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.order.ExpressApi;
import com.tiger.socol.gu.api.order.ExpressEntity;
import com.tiger.socol.gu.network.ObjectRequest;

public class OrderLogisticsPresenter extends MvpBasePresenter<OrderLogisticsView> {

    public void requestList(int orderId) {
        ExpressApi api = new ExpressApi();
        api.orderId = orderId;
        api.request(new ObjectRequest.OnRequestListeren<ExpressEntity>() {
            @Override
            public void onSuccess(ExpressEntity value) {
                if (getView() == null) return;
                getView().onRequestExpressSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestExpressFailure(message);
            }
        });
    }

}
