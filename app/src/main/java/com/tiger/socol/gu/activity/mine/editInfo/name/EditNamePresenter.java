package com.tiger.socol.gu.activity.mine.editInfo.name;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.menber.EditApi;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.ObjectRequest;

public class EditNamePresenter extends MvpBasePresenter<EditNameView> {

    public void changeName(String name) {
        EditApi api =  new EditApi();
        api.nickName = name;
        api.request(new ObjectRequest.OnRequestListeren<Member>() {
            @Override
            public void onSuccess(Member value) {
                if (getView() == null) return;
                MemberMannager.getInstance().updataUser(value);
                getView().onEditNameSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onEditNameFailure(message);
            }
        });
    }

}
