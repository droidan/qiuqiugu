package com.tiger.socol.gu.activity.mine.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.bind.PlatInfo;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends BaseViewStateActivity<RegistView, RegistPresenter>
        implements RegistView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_phone)
    EditText edPhone;

    private PlatInfo platInfo;

    @NonNull
    @Override
    public RegistPresenter createPresenter() {
        return new RegistPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_one;
    }

    @Override
    protected void initData() {
        platInfo = getIntent().getParcelableExtra(IntentConstant.PLAT_INFO);
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
    protected void afterViewInit() {
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnClick(R.id.bt_getcode)
    public void onClick() {
        String phone = edPhone.getText().toString();
        if (!getPresenter().checkPhone(phone)) {
            showToask("请输入正确手机号");
            return;
        }

        showProgress();
        getPresenter().sendSMSCode(phone);
    }

    @Override
    public void onSendSMSCodeSuccess(String phone) {
        dismissProgress();

        Intent intent = new Intent();
        intent.putExtra(IntentConstant.PHONE, phone);
        intent.setClass(this, RegistTwoActivity.class);
        if (platInfo != null) {
            intent.putExtra(IntentConstant.PLAT_INFO, platInfo);
        }
        startActivity(intent);
    }

    @Override
    public void onSendSMSCodeFailure(String message) {
        dismissProgress();
        showToask(message);
    }

}

