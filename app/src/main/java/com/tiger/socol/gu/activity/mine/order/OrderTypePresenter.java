package com.tiger.socol.gu.activity.mine.order;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.order.ListApi;
import com.tiger.socol.gu.api.order.OrderListEntity;
import com.tiger.socol.gu.network.FlowEntity;
import com.tiger.socol.gu.network.FlowRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderTypePresenter extends MvpBasePresenter<OrderTypeView> {

    private int page = 0;
    private List<OrderListEntity> orders = new ArrayList<>();

    private void requestList(String orderStatus) {
        ListApi api = new ListApi();
        api.page = page;
        api.orderStatus = orderStatus;
        api.request(new FlowRequest.OnRequestListeren<OrderListEntity>() {
            @Override
            public void onSuccess(FlowEntity<OrderListEntity> value) {
                if (getView() == null) return;
                if (page == 0) {
                    orders.clear();
                    orders.addAll(value.getData());
                    getView().onRequestFistPageSuccess(orders, value.isNextPage());
                } else {
                    orders.addAll(value.getData());
                    getView().onRequestNextPageSuccess(orders, value.isNextPage());
                }
                page++;
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                if (page == 0) {
                    getView().onRequestFistPageFailure(message);
                } else {
                    getView().onRequestNextPageFailure(message);
                }
            }
        });
    }

    public void requestFirstPage(String orderStatus) {
        page = 0;
        requestList(orderStatus);
    }

    public void requestNextPage(String orderStatus) {
        requestList(orderStatus);
    }

}
