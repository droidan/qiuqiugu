package com.tiger.socol.gu.activity.mine.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.bind.PlatInfo;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistTwoActivity extends BaseViewStateActivity<RegistTwoView, RegistTwoPresenter>
        implements RegistTwoView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_code)
    EditText edCode;

    private String phone;
    private PlatInfo platInfo;

    @NonNull
    @Override
    public RegistTwoPresenter createPresenter() {
        return new RegistTwoPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_two;
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra(IntentConstant.PHONE);
        platInfo = getIntent().getParcelableExtra(IntentConstant.PLAT_INFO);
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @OnClick(R.id.bt_submit)
    public void onClick() {
        String code = edCode.getText().toString();
        if (!presenter.codeIsEmpty(code)) {
            showToask("请输入验证码");
            return;
        }

        showProgress();
        presenter.validCode(phone, code);
    }

    @Override
    public void onCheckCodeSuccess() {
        dismissProgress();
        Intent intent =  new Intent();
        intent.putExtra(IntentConstant.PHONE, phone);
        intent.setClass(this, RegistThreeActivity.class);
        if (platInfo != null) {
            intent.putExtra(IntentConstant.PLAT_INFO, platInfo);
        }
        startActivity(intent);
    }

    @Override
    public void onCheckCodeFailure(String message) {
        dismissProgress();
        showToask(message);
    }

}

