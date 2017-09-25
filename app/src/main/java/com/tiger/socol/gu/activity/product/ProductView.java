package com.tiger.socol.gu.activity.product;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.index.FarmEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.List;

interface ProductView extends MvpView {

    void onRequestFristPageSuccess(List<GoodsEntity> goods, boolean more);

    void onRequestFristPageFailure(String message);

    void onRequestNextPageSuccess(List<GoodsEntity> goods, boolean more);

    void onRequestNextPageFailure(String message);

    void onRequestFarmSuccess(List<FarmEntity> farms);

    void onRequestFarmFailure(String message);

}