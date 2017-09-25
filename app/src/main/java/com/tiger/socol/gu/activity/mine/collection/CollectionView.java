package com.tiger.socol.gu.activity.mine.collection;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.List;

interface CollectionView extends MvpView {

    void onRequestFristPageSuccess(List<GoodsEntity> goods, boolean more);

    void onRequestFristPageFailure(String message);

    void onRequestNextPageSuccess(List<GoodsEntity> goods, boolean more);

    void onRequestNextPageFailure(String message);
}