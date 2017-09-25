package com.tiger.socol.gu.activity.mine.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.RegistThreeActivity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPasswordTwoActivity extends BaseViewStateActivity<FindPasswordTwoView, FindPasswordTwoPresenter>
        implements FindPasswordTwoView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_code)
    EditText edCode;

    private String phone;

    @NonNull
    @Override
    public FindPasswordTwoPresenter createPresenter() {
        return new FindPasswordTwoPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_password_two;
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra(IntentConstant.PHONE);
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

    @OnClick(R.id.bt_submit)
    public void onClick() {
        String code = edCode.getText().toString();
        showProgress();
        presenter.validCode(phone, code);
    }

    @Override
    public void onCheckCodeSuccess() {
        dismissProgress();
        String code = edCode.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(IntentConstant.PHONE, phone);
        intent.putExtra(IntentConstant.CODE, code);
        intent.setClass(this, FindPasswordThreeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCheckCodeFailure(String message) {
        dismissProgress();
        showToask(message);
    }

}
