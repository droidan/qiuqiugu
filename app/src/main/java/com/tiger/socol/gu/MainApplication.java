package com.tiger.socol.gu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;

public class MainApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        mContext = getApplicationContext();
        Realm.init(mContext);
    }

    public static Context getContext() {
        return mContext;
    }

}