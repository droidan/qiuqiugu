package com.tiger.socol.gu.activity.mine.about;

import android.support.v7.widget.Toolbar;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.base.BaseActivity;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("关于我们");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
