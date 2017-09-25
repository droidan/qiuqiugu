package com.tiger.socol.gu.activity.mine.login.bind;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.activity.mine.login.RegistActivity;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.OnClick;

public class BindActivity extends BaseViewStateActivity<BindView, BindPresenter>
        implements BindView {

    @butterknife.BindView(R.id.toolbar)
    Toolbar toolbar;
    @butterknife.BindView(R.id.ed_phone)
    EditText edPhone;
    @butterknife.BindView(R.id.ed_code)
    EditText edCode;

    private PlatInfo platInfo;

    @NonNull
    @Override
    public BindPresenter createPresenter() {
        return new BindPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind;
    }

    @Override
    protected void initData() {
        platInfo = getIntent().getParcelableExtra(IntentConstant.PLAT_INFO);
    }

    @Override
    protected void afterViewInit() {
        if (platInfo == null) {
            toolbar.setTitle("修改手机号");
        } else {
            toolbar.setTitle("绑定手机号");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.bt_getcode, R.id.bt_submit, R.id.bt_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_getcode:
                String type = "";
                if (platInfo == null) {
                    type = "rebind";
                } else {
                    type = "bind";
                }
                presenter.getCode(edPhone.getText().toString(), type);
                break;

            case R.id.bt_bind:
                Intent intent = new Intent();
                intent.setClass(this, RegistActivity.class);
                intent.putExtra(IntentConstant.PLAT_INFO, platInfo);
                startActivity(intent);
                break;

            case R.id.bt_submit:
                showProgress();
                String code = edCode.getText().toString();
                String phone = edPhone.getText().toString();
                if (platInfo == null) {
                    presenter.rebind(phone, code);
                } else {
                    presenter.bind(platInfo.getUserId(), phone, code);
                }
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
    public void onBindSuccess() {
        dismissProgress();
        if (platInfo == null) {
            // 正常用户切换手机号
            showToask("修改手机号成功");
            finish();
        } else {
            // 第三方登录绑定手机号
            showToask("绑定手机号成功");
            EventBus.getDefault().post(new LoginEvent());
        }
    }

    @Override
    public void onBindFailure(String message) {
        dismissProgress();
        showToask(message);
    }

    @Override
    public void onSendSMSStatus(String message) {
        showToask(message);
    }

}
