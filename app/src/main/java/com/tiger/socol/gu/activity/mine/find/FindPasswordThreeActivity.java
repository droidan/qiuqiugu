package com.tiger.socol.gu.activity.mine.find;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPasswordThreeActivity extends BaseViewStateActivity<FindPasswordThreeView, FindPasswordThreePresenter>
        implements FindPasswordThreeView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_password)
    EditText edPassword;

    private String code;
    private String phone;

    @NonNull
    @Override
    public FindPasswordThreePresenter createPresenter() {
        return new FindPasswordThreePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_password_three;
    }

    @Override
    protected void initData() {
        code = getIntent().getStringExtra(IntentConstant.CODE);
        phone = getIntent().getStringExtra(IntentConstant.PHONE);
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("找回密码");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.bt_submit)
    public void onClick() {
        showProgress();
        String password = edPassword.getText().toString();
        presenter.find(phone, code, password);
    }

    @Override
    public void onFindPasswordSuccess() {
        dismissProgress();
        showToask("修改成功");
        EventBus.getDefault().post(new FindEvent());
        finish();
    }

    @Override
    public void onFindPasswordFailure(String message) {
        dismissProgress();
        showToask(message);
    }

}
