package com.tiger.socol.gu.wxapi;

import com.blankj.utilcode.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tiger.socol.gu.activity.mine.order.OrderEvent;
import com.tiger.socol.gu.constant.Constant;
import com.tiger.socol.gu.utils.PayEvent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                // 支付成功
                ToastUtils.showShortToast(this, "支付成功");
                EventBus.getDefault().post(new PayEvent());
                EventBus.getDefault().post(new OrderEvent(0));
            } else {
                // 支付失败
                ToastUtils.showShortToast(this, "支付失败" + resp.errStr + "," + resp.errCode);
            }
            finish();
        }
    }

}