package com.tiger.socol.gu.activity.mine.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.mob.tools.utils.UIHandler;
import com.tiger.socol.gu.activity.mine.login.bind.BindActivity;
import com.tiger.socol.gu.activity.mine.login.bind.PlatInfo;
import com.tiger.socol.gu.api.menber.LoginAip;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.api.menber.ThreeLoginApi;
import com.tiger.socol.gu.api.menber.ThreeLoginEntity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.ObjectRequest;
import com.tiger.socol.gu.utils.ToastUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginPresenter extends MvpBasePresenter<LoginView> implements PlatformActionListener, Handler.Callback {

    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    public void login(String phone, String password) {
        if (StringUtils.isEmpty(phone)) {
            getView().onLoginFailure("请输入手机号");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            getView().onLoginFailure("请输入密码");
            return;
        }

        LoginAip api = new LoginAip();
        api.phone = phone;
        api.password = password;
        api.request(new ObjectRequest.OnRequestListeren<Member>() {

            @Override
            public void onSuccess(Member value) {
                if (getView() == null) return;
                MemberMannager.getInstance().savaMember(value);
                getView().onLoginSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onLoginFailure(message);
            }
        });
    }

    public void weixinLogin() {
        authorize(new Wechat(context));
    }

    public void qqLogin() {
        authorize(new QQ(context));
    }

    private void authorize(Platform plat) {
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            PlatInfo info = new PlatInfo();
            info.setPlatName(platform.getName());
            info.setUserId(platform.getDb().getUserId());
            info.setNikeName(platform.getDb().getUserName());
            info.setUserAvatar(platform.getDb().getUserIcon());

            ToastUtils.show(context, info.toString());

            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = info;
            UIHandler.sendMessage(msg, this);
        }
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL:
                getView().onLoginFailure("取消登录");
                break;
            case MSG_AUTH_ERROR:
                getView().onLoginFailure("登录失败");
                break;
            case MSG_AUTH_COMPLETE:
                threeLogin((PlatInfo) msg.obj);
                break;
        }
        return false;
    }

    public void threeLogin(final PlatInfo platInfo) {
        ThreeLoginApi api = new ThreeLoginApi();
        api.platform = platInfo.getPlatName();
        api.figureUrl = platInfo.getUserAvatar();
        api.openId = platInfo.getUserId();
        api.nickName = platInfo.getPlatName();
        api.request(new ObjectRequest.OnRequestListeren<ThreeLoginEntity>() {
            @Override
            public void onSuccess(ThreeLoginEntity value) {
                if (getView() == null) return;
                if (value.isBind()) {
                    MemberMannager.getInstance().savaMember(value.getData());
                    getView().onLoginSuccess(value.getData());
                } else {
                    Intent intent = new Intent();
                    intent.setClass(context, BindActivity.class);
                    intent.putExtra(IntentConstant.PLAT_INFO, platInfo);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onLoginFailure(message);
            }
        });
    }

}