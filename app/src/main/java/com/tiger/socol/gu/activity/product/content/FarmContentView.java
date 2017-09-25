package com.tiger.socol.gu.activity.product.content;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.farm.FarmDetailEntity;

interface FarmContentView extends MvpView {
    void onRequestContentSuccess(FarmDetailEntity entity);

    void onRequestContentFailure(String message);
}