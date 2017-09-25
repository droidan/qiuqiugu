package com.tiger.socol.gu.activity.mine.order.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.order.RefundEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefundActivity extends BaseViewStateActivity<RefundView, RefundPresenter>
        implements RefundView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tx_porder_title)
    TextView txPorderTitle;
    @BindView(R.id.tx_product_num)
    TextView txProductNum;
    @BindView(R.id.im_product_thumb)
    ImageView imProductThumb;
    @BindView(R.id.tx_product_time)
    TextView txProductTime;
    @BindView(R.id.tx_price)
    TextView txPrice;
    @BindView(R.id.tx_time)
    TextView txTime;
    @BindView(R.id.tx_status)
    TextView txStatus;
    @BindView(R.id.tx_context)
    TextView txContext;

    private String sn;
    private int orderId;

    @NonNull
    @Override
    public RefundPresenter createPresenter() {
        return new RefundPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refund;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("退款");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderId = getIntent().getIntExtra(IntentConstant.ORDER_ID, 0);
        sn = getIntent().getStringExtra(IntentConstant.SN);

        showProgress();
        presenter.refund(orderId, sn);
    }

    @Override
    public void onRequestRefundSuccess(RefundEntity refund) {
        dismissProgress();

        txPrice.setText(refund.getTotalPrice() + "");
        txPorderTitle.setText(refund.getGoods().get(0).getGoodsName());
        txProductTime.setText(refund.getCreated());

        txTime.setText(refund.getRefundData().getCreated());
        txContext.setText(refund.getRefundData().getCaseX());
        txStatus.setText(refund.getRefundData().getStatus());

        String thumb = refund.getGoods().get(0).getThumb();
        if (!StringUtils.isEmpty(thumb)) {
            Picasso.with(this).load(thumb).into(imProductThumb);
        }
    }

    @Override
    public void onRequestRefundFailure(String message) {
        showToask(message);
        dismissProgress();
    }

}
