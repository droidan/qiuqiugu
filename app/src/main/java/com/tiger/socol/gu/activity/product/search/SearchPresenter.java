package com.tiger.socol.gu.activity.product.search;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.api.index.KeywordApi;
import com.tiger.socol.gu.api.index.KeywordData;
import com.tiger.socol.gu.api.index.SearchApi;
import com.tiger.socol.gu.managers.KeywordEntity;
import com.tiger.socol.gu.network.FlowEntity;
import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter extends MvpBasePresenter<SearchAView> {

    private int page = 0;
    private List<GoodsEntity> goods = new ArrayList<>();

    public void search(String keyword) {
        SearchApi api = new SearchApi();
        api.keyword = keyword;
        api.request(new FlowRequest.OnRequestListeren<GoodsEntity>() {
            @Override
            public void onSuccess(FlowEntity<GoodsEntity> value) {
                if (getView() == null) return;
                if (page == 0) {
                    goods.clear();
                }
                page++;
                goods.addAll(value.getData());
                getView().searchSuccess(goods, value.isNextPage());
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().searchFailure(message);
            }
        });
    }

    public void searchFisrt(String keyword) {
        page = 0;
        search(keyword);
    }

    public void searchNext(String keyword) {
        search(keyword);
    }

    public void requestKeywords() {
        KeywordApi api = new KeywordApi();
        api.request(new ObjectRequest.OnRequestListeren<KeywordData>() {
            @Override
            public void onSuccess(KeywordData value) {
                if (getView() == null) return;
                getView().requestKeywordsSuccess(value.getKeyword());
            }

            @Override
            public void onFailure(String message) {

            }
        });

    }

}