package com.tiger.socol.gu.activity.mine.login.bind;


import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.menber.BindApi;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.api.menber.RebindApi;
import com.tiger.socol.gu.api.menber.SendSMSCode;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.network.ObjectRequest;

public class BindPresenter extends MvpBasePresenter<BindView> {

    public void getCode(String phone, String type) {
        if (StringUtils.isEmpty(phone)) {
            getView().onSendSMSStatus("请输入手机号");
            return;
        }

        SendSMSCode api = new SendSMSCode();
        api.phone = phone;
        api.useAge = type;
        api.request(new NullRequest.OnRequestListeren() {

            @Override
            public void onSuccess() {
                if (getView() == null) return;
                getView().onSendSMSStatus("发送成功");
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onSendSMSStatus(message);
            }
        });
    }

    public void rebind(String phone, String code) {
        if (StringUtils.isEmpty(phone)) {
            getView().onBindFailure("请输入手机号");
            return;
        }
        if (StringUtils.isEmpty(code)) {
            getView().onBindFailure("请输入验证码");
            return;
        }
        RebindApi api = new RebindApi();
        api.code = code;
        api.phone = phone;
        api.request(new ObjectRequest.OnRequestListeren<MsgEntity>() {
            @Override
            public void onSuccess(MsgEntity value) {
                if (getView() == null) return;
                getView().onBindSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onBindFailure(message);
            }
        });
    }

    public void bind(String openId, String phone, String code) {
        if (StringUtils.isEmpty(phone)) {
            getView().onBindFailure("请输入手机号");
            return;
        }
        if (StringUtils.isEmpty(code)) {
            getView().onBindFailure("请输入验证码");
            return;
        }

        BindApi api = new BindApi();
        api.code = code;
        api.phone = phone;
        api.openId = openId;
        api.request(new ObjectRequest.OnRequestListeren<Member>() {
            @Override
            public void onSuccess(Member value) {
                if (getView() == null) return;
                MemberMannager.getInstance().savaMember(value);
                getView().onBindSuccess();
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onBindFailure(message);
            }
        });
    }

}