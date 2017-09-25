package com.tiger.socol.gu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.utils.*;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tiger.socol.gu.activity.mine.order.OrderEvent;
import com.tiger.socol.gu.activity.shopCart.root.CartEvent;
import com.tiger.socol.gu.constant.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.xml.transform.Result;

public class PayUtils {

    private static PayUtils single = null;
    private static final int SDK_PAY_FLAG = 1;
    private Activity context;

    private PayUtils() {
    }

    public static PayUtils getInstance() {
        if (single == null) {
            synchronized (PayUtils.class) {
                if (single == null) {
                    single = new PayUtils();
                }
            }
        }
        return single;
    }

    public void wxPay(Context context, PayReq req) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, Constant.WECHAT_APP_ID, false);
        api.registerApp(Constant.WECHAT_APP_ID);
        api.sendReq(req);
    }

    public void aliPay(final String order, final Activity context) {
        this.context = context;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(order, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
                // resultStatus = 9000
                // memo
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    Map<String, String> result = (Map<String, String>) msg.obj;
                    String status = result.get("resultStatus");
                    String memo = result.get("memo");
                    if (Integer.valueOf(status) == 9000) {
                        EventBus.getDefault().post(new PayEvent());
                        EventBus.getDefault().post(new OrderEvent(0));
                        EventBus.getDefault().post(new OrderEvent(1));
                        com.blankj.utilcode.utils.ToastUtils.showShortToast(context, "支付成功");
                    } else {
                        com.blankj.utilcode.utils.ToastUtils.showShortToast(context, "支付失败");
                    }
                    break;
            }
        }
    };

}
