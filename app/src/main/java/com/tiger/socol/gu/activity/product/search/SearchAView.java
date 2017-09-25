package com.tiger.socol.gu.activity.product.search;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.api.menber.Member;

import java.util.List;

interface SearchAView extends MvpView {
    void searchSuccess(List<GoodsEntity> goods, boolean more);

    void searchFailure(String message);

    void requestKeywordsSuccess(List<String> keywords);
}