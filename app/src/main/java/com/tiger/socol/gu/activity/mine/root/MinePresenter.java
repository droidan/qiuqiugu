package com.tiger.socol.gu.activity.mine.root;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.menber.LogoutApi;
import com.tiger.socol.gu.api.order.NumApi;
import com.tiger.socol.gu.api.order.OrderNumEntity;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.network.ObjectRequest;

public class MinePresenter extends MvpBasePresenter<MineView> {

    public void logout() {
        LogoutApi api = new LogoutApi();
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                if (getView() == null) return;
                MemberMannager.getInstance().logout();
                getView().onLogoutSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onLogoutFailure(message);
            }
        });
    }

    public void orderNum() {
        NumApi api = new NumApi();
        api.request(new ObjectRequest.OnRequestListeren<OrderNumEntity>() {
            @Override
            public void onSuccess(OrderNumEntity value) {
                if (getView() == null) return;
                getView().onReloadNumSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onReloadNumFailure(message);
            }
        });
    }

}
