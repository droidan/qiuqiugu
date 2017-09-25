package com.tiger.socol.gu.activity.mine.order.content;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.order.ExpressEntity;
import com.tiger.socol.gu.base.BaseViewStateFragment;
import com.tiger.socol.gu.constant.IntentConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderLogisticsFragment extends BaseViewStateFragment<OrderLogisticsView, OrderLogisticsPresenter> implements OrderLogisticsView {

    @BindView(R.id.tx_order_id)
    TextView txOrderId;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tx_title)
    TextView txTitle;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    private int orderId;

    private OrderLogisticsAdapter adapter;

    @Override
    protected void initData() {

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        orderId = getArguments() != null ? getArguments().getInt(IntentConstant.ORDER_ID) : 0;
    }

    public static OrderLogisticsFragment newInstance(int orderId) {
        OrderLogisticsFragment fragment = new OrderLogisticsFragment();
        Bundle args = new Bundle();
        args.putInt(IntentConstant.ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void aftertView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OrderLogisticsAdapter();
        recyclerView.setAdapter(adapter);

        presenter.requestList(orderId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_logistics;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public OrderLogisticsPresenter createPresenter() {
        return new OrderLogisticsPresenter();
    }

    @Override
    public void onRequestExpressSuccess(ExpressEntity value) {
        if (value.getExpressNo() == null) {
            showToask("暂无物流信息");
            activityMain.setVisibility(View.GONE);
            return;
        }
        txTitle.setVisibility(View.VISIBLE);
        txOrderId.setText(value.getExpressNo() + "");
        adapter.setExpresss(value.getData());
    }

    @Override
    public void onRequestExpressFailure(String message) {

    }
}