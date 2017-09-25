package com.tiger.socol.gu.activity.shopCart.root;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.List;

interface ShopCartView extends MvpView {

    void onRequestCartListSuccess(List<GoodsEntity> goods);

    void onRequestCartListFailure(String message);

    void onDeleteSuccess(GoodsEntity goodsEntity);

    void onDeleteFailure(String message);
}