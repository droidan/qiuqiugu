package com.tiger.socol.gu.activity.shopCart.order;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.address.AddressEntity;
import com.tiger.socol.gu.api.menber.VoucherEntity;
import com.tiger.socol.gu.api.order.ExpressCostEntity;
import com.tiger.socol.gu.api.order.OrderEntity;

import java.util.List;

interface CartContentView extends MvpView {

    void onRequestAddressSuccess(AddressEntity address);

    void onRequestAddressFailure(String message);

    void onRequestVoucherListSuccess(List<VoucherEntity> vouchers);

    void onRequestVoucherListFailure(String message);

    void onRequestPostageSuccess(ExpressCostEntity entity);

    void onRequestPostageFailure(String message);

    void orderPaySuccess(OrderEntity orderEntity);

    void orderPayFailure(String message);

}