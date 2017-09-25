package com.tiger.socol.gu.activity.product.content;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.good.GoodDetailEntity;

interface ProductContentView extends MvpView {

    void onRequestContentSuccess(GoodDetailEntity entity);

    void onRequestContentFailure(String message);

    void onCollectSuccess(boolean collect);

    void onCollectFailure(String message);

    void onAddCartSuccess(String message);

    void onAddCaetFailure(String message);

}