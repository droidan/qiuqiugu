package com.tiger.socol.gu.utils;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import rx.Subscriber;

/**
 * 权限操作工具
 * Created by jian_zhou on 2016/9/13.
 */
public class PermissionUtils {

    private Activity activity;
    private onPermissionResultListener listener;
    public final int Permission_Request_Code = 0x1;
    /**
     * 读写sdk权限
     */
    public static final String Permission_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private static PermissionUtils PermissionUtils;

    public static PermissionUtils getIntence(Activity context) {
        if (PermissionUtils == null) {
            synchronized (PermissionUtils.class) {
                if (PermissionUtils == null)
                    PermissionUtils = new PermissionUtils(context);
            }
        }
        return PermissionUtils;
    }

    private PermissionUtils(Activity activity) {
        this.activity = activity;
    }

    public PermissionUtils requestPerssion(final String permission) {
        RxPermissions.get(activity).observe(permission).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    if (listener != null)
                        listener.onPermit();
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, Permission_Request_Code);
                }
            }
        });

        return this;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Permission_Request_Code:
                if (permissions.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    if (listener != null)
                        listener.onPermit();
                } else {
                    if (listener != null)
                        listener.onRefuse();
                }
                break;

            default:
                if (listener != null)
                    listener.onRefuse();
                break;
        }
    }

    public PermissionUtils setPermissionResultListener(onPermissionResultListener listener) {
        this.listener = listener;
        return this;
    }

    public interface onPermissionResultListener {
        /**
         * 权限被拒绝
         */
        void onRefuse();

        /**
         * 已经获得权限
         */
        void onPermit();
    }

}