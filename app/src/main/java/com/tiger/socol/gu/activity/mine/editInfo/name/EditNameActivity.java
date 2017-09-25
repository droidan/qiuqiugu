package com.tiger.socol.gu.activity.mine.editInfo.name;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.managers.MemberMannager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNameActivity extends BaseViewStateActivity<EditNameView, EditNamePresenter>
        implements EditNameView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_name)
    EditText edName;

    @NonNull
    @Override
    public EditNamePresenter createPresenter() {
        return new EditNamePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_name;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("修改昵称");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_bt_submit) {
                    String name = edName.getText().toString();
                    if (StringUtils.isEmpty(name)) {
                        showToask("请输入昵称");
                        return true;
                    }

                    showProgress();
                    presenter.changeName(name);
                }
                return true;
            }
        });

        Member member = MemberMannager.getInstance().getLoginUser();
        edName.setText(member.getNickName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public void onEditNameSuccess() {
        dismissProgress();
        finish();
        showToask("修改成功");
    }

    @Override
    public void onEditNameFailure(String message) {
        dismissProgress();
        showToask(message);
    }

}

