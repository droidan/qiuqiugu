package com.tiger.socol.gu.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.managers.AppManager;
import com.tiger.socol.gu.utils.ToastUtils;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;


/**
 * 基类
 *
 * @author jian_zhou
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 接收器
     */
    private MyReceiver receiver;
    /**
     * 广播action
     */
    public static final String SYSTEM_EXIT = "com.exitsystem.system_exit";

    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播，用于退出程序
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYSTEM_EXIT);
        receiver = new MyReceiver();
        this.registerReceiver(receiver, filter);

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initData();
        afterViewInit();
    }

    /**
     * 得到layout布局文件，R.layout.activity_xxxx
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据，包括从bundle中获取数据保存到当前activity中
     */
    protected abstract void initData();

    /**
     * 界面初始化之后的后处理，如启动网络读取数据、启动定位等
     */
    protected abstract void afterViewInit();

    protected void showProgress() {
        if (dialog == null) {
            dialog = new SpotsDialog(this, "加载中...");
        }
        dialog.show();
    }

    protected void dismissProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void showToask(int msgID) {
        showToask(getString(msgID));
    }

    protected void showToask(String msg) {
        ToastUtils.show(this, msg);
    }

    protected void finishActi(Activity acti) {
        AppManager.getAppManager().finishActivity(acti);
    }

    protected void startActi(Class<?> acti) {
        startActi(null, acti);
    }

    protected void startActi(Bundle bundle, Class<?> acti) {
        Intent intent = new Intent(this, acti);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        //取消用于退出程序的广播注册
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 安全退出程序的广播接收者
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}