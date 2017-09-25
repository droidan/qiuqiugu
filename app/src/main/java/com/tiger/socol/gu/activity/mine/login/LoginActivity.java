package com.tiger.socol.gu.activity.mine.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.find.FindPasswordOneActivity;
import com.tiger.socol.gu.activity.mine.login.bind.BindActivity;
import com.tiger.socol.gu.activity.mine.login.bind.PlatInfo;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.base.BaseViewStateActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseViewStateActivity<LoginView, LoginPresenter>
        implements LoginView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_password)
    EditText edPassword;

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.bt_login, R.id.bt_regist, R.id.bt_find, R.id.bt_qq_login, R.id.bt_weixin_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                showProgress();
                String phone = edPhone.getText().toString();
                String password = edPassword.getText().toString();
                presenter.login(phone, password);
                break;

            case R.id.bt_regist:
                startActi(RegistActivity.class);
                break;

            case R.id.bt_find:
                startActi(FindPasswordOneActivity.class);
                break;

            case R.id.bt_qq_login:
                presenter.qqLogin();
                break;

            case R.id.bt_weixin_login:
                presenter.weixinLogin();
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoginSuccess(Member member) {
        dismissProgress();
        showToask("登录成功");
        finish();
    }

    @Override
    public void onLoginFailure(String message) {
        dismissProgress();
        showToask(message);
    }

}

