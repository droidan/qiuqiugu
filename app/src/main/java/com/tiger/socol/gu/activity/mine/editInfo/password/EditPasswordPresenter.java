package com.tiger.socol.gu.activity.mine.editInfo.password;

import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.menber.EditApi;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.api.menber.ResetpwApi;
import com.tiger.socol.gu.network.ObjectRequest;

public class EditPasswordPresenter extends MvpBasePresenter<EditPasswordView> {

    public void editPassword(String oldpassword, String newpassword) {
        if (StringUtils.isEmpty(oldpassword)) {
            getView().onEditPasswordFailure("请输入旧密码");
            return;
        }

        if (StringUtils.isEmpty(newpassword)) {
            getView().onEditPasswordFailure("请输入新密码");
            return;
        }

        if (newpassword.length() < 6) {
            getView().onEditPasswordFailure("密码不能少于6位");
            return;
        }

        ResetpwApi api = new ResetpwApi();
        api.newPassword = newpassword;
        api.oldPassword = oldpassword;
        api.request(new ObjectRequest.OnRequestListeren<MsgEntity>() {
            @Override
            public void onSuccess(MsgEntity value) {
                if (getView() == null) return;
                getView().onEditPasswordSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onEditPasswordFailure(message);
            }
        });

    }

}
