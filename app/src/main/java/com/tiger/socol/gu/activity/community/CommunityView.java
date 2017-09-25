package com.tiger.socol.gu.activity.community;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tiger.socol.gu.api.bbs.AdEntity;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.network.FlowEntity;

import java.util.List;

interface CommunityView extends MvpView {

    void onRequestAdsSuccess(List<AdEntity> ads);

    void onRequestAdsFailure(String message);

    void onRequestFirstBBSListSuccess(List<BBSEntity> flow, boolean more);

    void onRequestFirstBBSListFailure(String message);

    void onRequestNextBBSListSuccess(List<BBSEntity> flow, boolean more);

    void onRequestNextBBSListFailure(String message);

}