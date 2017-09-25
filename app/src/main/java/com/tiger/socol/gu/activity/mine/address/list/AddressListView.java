package com.tiger.socol.gu.activity.mine.address.list;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.address.AddressEntity;

import java.util.List;

interface AddressListView extends MvpView {
    void onRequestListSuccess(List<AddressEntity> list);

    void onRequestListFailure(String message);

    void onDeteleAddressSuccess(AddressEntity addressEntity);

    void onDeteleAddressFailure(String message);
}