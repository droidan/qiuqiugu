package com.tiger.socol.gu;

import android.os.Handler;

import com.tiger.socol.gu.MainActivity;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.base.BaseActivity;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                finish();
                startActi(MainActivity.class);
            }
        }, 1000);
    }

    @Override
    protected void afterViewInit() {

    }

}
