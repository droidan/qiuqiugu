package com.tiger.socol.gu.activity.product;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.cart.AddApi;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.good.CollectApi;
import com.tiger.socol.gu.api.index.FarmEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.api.index.ListApi;
import com.tiger.socol.gu.api.index.SlideApi;
import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.FlowEntity;
import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.ArrayList;
import java.util.List;

public class ProductPresenter extends MvpBasePresenter<ProductView> {

    private int page = 0;
    private List<GoodsEntity> goods = new ArrayList<>();

    private void goodList() {
        ListApi api = new ListApi();
        api.page = page;
        api.request(new FlowRequest.OnRequestListeren<GoodsEntity>() {
            @Override
            public void onSuccess(FlowEntity<GoodsEntity> value) {
                if (getView() == null) return;
                if (page == 0) {
                    goods.clear();
                    goods.addAll(value.getData());
                    getView().onRequestFristPageSuccess(goods, value.isNextPage());
                } else {
                    goods.addAll(value.getData());
                    getView().onRequestNextPageSuccess(goods, value.isNextPage());
                }
                page++;
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                if (page == 0) {
                    getView().onRequestFristPageFailure(message);
                } else {
                    getView().onRequestNextPageFailure(message);
                }
            }
        });
    }

    public void requestFristPage() {
        page = 0;
        goodList();
    }

    public void requestNextPage() {
        goodList();
    }

    public void farmList() {
        SlideApi api = new SlideApi();
        api.request(new ArrayRequest.OnRequestListeren<FarmEntity>() {
            @Override
            public void onSuccess(List<FarmEntity> value) {
                if (getView() == null) return;
                getView().onRequestFarmSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestFarmFailure(message);
            }
        });
    }

}
