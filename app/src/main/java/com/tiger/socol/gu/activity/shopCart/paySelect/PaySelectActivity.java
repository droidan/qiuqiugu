package com.tiger.socol.gu.activity.shopCart.paySelect;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.alipay.sdk.app.EnvUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.shopCart.root.CartEvent;
import com.tiger.socol.gu.api.order.WechaPayApi;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.utils.PayEvent;
import com.tiger.socol.gu.utils.PayUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySelectActivity extends BaseViewStateActivity<PaySelectView, PaySelectPresenter>
        implements PaySelectView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cb_select)
    CheckBox cbSelect;
    @BindView(R.id.cb_select2)
    CheckBox cbSelect2;

    private String sn;
    private String sgin;
    private PayReq req;

    @NonNull
    @Override
    public PaySelectPresenter createPresenter() {
        return new PaySelectPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_select;
    }

    @Override
    protected void initData() {
        sn = getIntent().getStringExtra(IntentConstant.SN);
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("支付方式");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.rl_pay_ali, R.id.rl_pay_weixin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_pay_ali:
                // 支付宝
                selectAliPay(true);
                if (sgin != null) {
                    onRequestSignSuccess(sgin);
                } else {
                    showProgress();
                    presenter.requestSgin(sn, "alipay");
                }
                break;

            case R.id.rl_pay_weixin:
                // 微信支付
                selectAliPay(false);
                if (req != null) {
                    onRequestWechaSignSuccess(req);
                } else {
                    showProgress();
                    presenter.wechaPay(sn);
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(PayEvent event) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void selectAliPay(boolean select) {
        cbSelect.setChecked(select);
        cbSelect2.setChecked(!select);
    }

    @Override
    public void onRequestSignSuccess(String order) {
        sgin = order;
        dismissProgress();
//            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        PayUtils.getInstance().aliPay(order, this);
    }

    @Override
    public void onRequestWechaSignSuccess(PayReq req) {
        this.req = req;
        dismissProgress();
        PayUtils.getInstance().wxPay(this, req);
    }

    @Override
    public void onRequestSignFailure(String message) {
        dismissProgress();
    }

}
