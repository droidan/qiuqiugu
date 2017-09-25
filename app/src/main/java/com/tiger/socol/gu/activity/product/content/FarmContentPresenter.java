package com.tiger.socol.gu.activity.product.content;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.farm.DetailApi;
import com.tiger.socol.gu.api.farm.FarmDetailEntity;
import com.tiger.socol.gu.network.ObjectRequest;

public class FarmContentPresenter extends MvpBasePresenter<FarmContentView> {

    public void requestContent(int baseId) {
        DetailApi api = new DetailApi();
        api.baseId = baseId;
        api.request(new ObjectRequest.OnRequestListeren<FarmDetailEntity>() {
            @Override
            public void onSuccess(FarmDetailEntity value) {
                if (getView() == null) return;
                getView().onRequestContentSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestContentFailure(message);
            }
        });
    }

}
