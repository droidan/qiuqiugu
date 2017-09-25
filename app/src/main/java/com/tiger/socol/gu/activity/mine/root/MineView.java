package com.tiger.socol.gu.activity.mine.root;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.order.OrderNumEntity;

interface MineView extends MvpView {

    void onLogoutSuccess();

    void onLogoutFailure(String message);

    void onReloadNumSuccess(OrderNumEntity entity);

    void onReloadNumFailure(String message);
}