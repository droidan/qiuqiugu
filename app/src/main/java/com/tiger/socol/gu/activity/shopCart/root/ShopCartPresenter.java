package com.tiger.socol.gu.activity.shopCart.root;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.cart.DeteleApi;
import com.tiger.socol.gu.api.cart.ListApi;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;

public class ShopCartPresenter extends MvpBasePresenter<ShopCartView> {

    public void goodsList() {
        ListApi api = new ListApi();
        api.request(new ArrayRequest.OnRequestListeren<GoodsEntity>() {
            @Override
            public void onSuccess(List<GoodsEntity> value) {
                if (getView() == null) return;
                getView().onRequestCartListSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestCartListFailure(message);
            }
        });
    }

    public void delete(final GoodsEntity good) {
        DeteleApi api = new DeteleApi();
        api.goodsId = good.getGoodsId() + "";
        api.request(new ObjectRequest.OnRequestListeren<MsgEntity>() {
            @Override
            public void onSuccess(MsgEntity value) {
                if (getView() == null) return;
                getView().onDeleteSuccess(good);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onDeleteFailure(message);
            }
        });
    }

}
