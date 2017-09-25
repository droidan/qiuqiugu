package com.tiger.socol.gu.activity.mine.address.edit;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.address.AddressEntity;

interface EditAddressActivityView extends MvpView {
    void onEditAddressSuccess(AddressEntity address);

    void onEditAddressFailure(String message);

    void onAddAddressSuccess(AddressEntity address);

    void onAddAddressFailure(String message);

}