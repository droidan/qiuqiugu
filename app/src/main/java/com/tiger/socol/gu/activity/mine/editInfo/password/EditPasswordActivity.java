package com.tiger.socol.gu.activity.mine.editInfo.password;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.base.BaseViewStateActivity;

import butterknife.BindView;

public class EditPasswordActivity extends BaseViewStateActivity<EditPasswordView, EditPasswordPresenter>
        implements EditPasswordView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_new_password)
    EditText edNewPassword;

    @NonNull
    @Override
    public EditPasswordPresenter createPresenter() {
        return new EditPasswordPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_password;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String password = edPassword.getText().toString();
                String newPassword = edNewPassword.getText().toString();
                showProgress();
                presenter.editPassword(password, newPassword);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public void onEditPasswordSuccess() {
        dismissProgress();
        showToask("修改成功");
        finish();
    }

    @Override
    public void onEditPasswordFailure(String message) {
        dismissProgress();
        showToask(message);
    }

}

