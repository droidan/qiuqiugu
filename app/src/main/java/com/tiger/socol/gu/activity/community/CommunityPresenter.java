package com.tiger.socol.gu.activity.community;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.bbs.ADVApi;
import com.tiger.socol.gu.api.bbs.AdEntity;
import com.tiger.socol.gu.api.bbs.AddCommentApi;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.api.bbs.ListApi;
import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.FlowEntity;
import com.tiger.socol.gu.network.FlowRequest;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.ArrayList;
import java.util.List;

public class CommunityPresenter extends MvpBasePresenter<CommunityView> {

    private int page;
    private List<BBSEntity> flows = new ArrayList<>();

    public void bbsList() {
        ListApi api = new ListApi();
        api.page = 1;
        api.request(new FlowRequest.OnRequestListeren<BBSEntity>() {
            @Override
            public void onSuccess(FlowEntity<BBSEntity> value) {
                if (getView() == null) return;
                if (page == 0) {
                    flows.clear();
                    flows.addAll(value.getData());
                    getView().onRequestFirstBBSListSuccess(flows, value.isNextPage());
                } else {
                    flows.addAll(value.getData());
                    getView().onRequestNextBBSListSuccess(flows, value.isNextPage());
                }
                page++;
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                if (page == 0) {
                    getView().onRequestFirstBBSListFailure(message);
                } else {
                    getView().onRequestNextBBSListFailure(message);
                }
            }
        });
    }


    public void requestFristPage() {
        page = 0;
        bbsList();
    }

    public void requestNextPage() {
        bbsList();
    }

    public void requestAds() {
        ADVApi api = new ADVApi();
        api.request(new ArrayRequest.OnRequestListeren<AdEntity>() {
            @Override
            public void onSuccess(List<AdEntity> value) {
                if (getView() == null) return;
                getView().onRequestAdsSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestAdsFailure(message);
            }
        });
    }

}
