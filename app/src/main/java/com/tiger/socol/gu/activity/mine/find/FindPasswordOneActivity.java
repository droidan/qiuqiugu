package com.tiger.socol.gu.activity.mine.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPasswordOneActivity extends BaseViewStateActivity<FindPasswordOneView, FindPasswordOnePresenter>
        implements FindPasswordOneView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_phone)
    EditText edPhone;

    @NonNull
    @Override
    public FindPasswordOnePresenter createPresenter() {
        return new FindPasswordOnePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_password_one;
    }

    @Override
    protected void initData() {

    }

    @Subscribe
    public void onEventMainThread(FindEvent event) {
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
    protected void afterViewInit() {
        toolbar.setTitle("找回密码");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.bt_getcode)
    public void onClick() {
        String phone = edPhone.getText().toString();
        showProgress();
        getPresenter().sendSMSCode(phone);
    }

    @Override
    public void onSendSMSCodeSuccess(String phone) {
        dismissProgress();
        Intent intent = new Intent();
        intent.putExtra(IntentConstant.PHONE, phone);
        intent.setClass(this, FindPasswordTwoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSendSMSCodeFailure(String message) {
        dismissProgress();
        showToask(message);
    }

}
