package com.tiger.socol.gu.activity.mine.address.list;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.address.AddressEntity;
import com.tiger.socol.gu.api.address.DeleteApi;
import com.tiger.socol.gu.api.address.ListApi;
import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.NullRequest;

import java.util.List;

public class AddressListPresenter extends MvpBasePresenter<AddressListView> {

    public void addressList() {
        ListApi api = new ListApi();
        api.request(new ArrayRequest.OnRequestListeren<AddressEntity>() {
            @Override
            public void onSuccess(List<AddressEntity> value) {
                if (getView() == null) return;
                getView().onRequestListSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestListFailure(message);
            }
        });
    }

    public void deteleAddress(final AddressEntity addressEntity) {
        DeleteApi api = new DeleteApi();
        api.addressId = addressEntity.getAddressId() + "";
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                if (getView() == null) return;
                getView().onDeteleAddressSuccess(addressEntity);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onDeteleAddressFailure(message);
            }
        });
    }

}
