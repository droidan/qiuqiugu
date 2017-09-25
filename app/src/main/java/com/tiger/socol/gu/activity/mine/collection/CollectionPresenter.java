package com.tiger.socol.gu.activity.mine.collection;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.good.CollectListApi;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.network.FlowEntity;
import com.tiger.socol.gu.network.FlowRequest;

import java.util.ArrayList;
import java.util.List;

public class CollectionPresenter extends MvpBasePresenter<CollectionView> {

    private int page = 0;
    private List<GoodsEntity> goods = new ArrayList<>();

    public void collectionList() {
        CollectListApi api = new CollectListApi();
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

    public void requestFrilstPage() {
        page = 0;
        collectionList();
    }

    public void requestNextPage() {
        collectionList();
    }

}
