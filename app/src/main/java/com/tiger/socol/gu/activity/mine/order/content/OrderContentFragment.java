package com.tiger.socol.gu.activity.mine.order.content;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.order.OrderEvent;
import com.tiger.socol.gu.activity.product.assess.AssessActivity;
import com.tiger.socol.gu.activity.shopCart.order.CartContentActivity;
import com.tiger.socol.gu.activity.shopCart.order.CartContentAdapter;
import com.tiger.socol.gu.activity.shopCart.paySelect.PaySelectActivity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.api.order.OrderDetailEntity;
import com.tiger.socol.gu.base.BaseViewStateFragment;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.views.widgets.FullyLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderContentFragment extends BaseViewStateFragment<OrderContentView, OrderContentPresenter>
        implements OrderContentView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.bt_left_menu)
    Button btLeftMenu;
    @BindView(R.id.bt_right_menu)
    Button btRightMenu;
    @BindView(R.id.ll_logistics_info)
    LinearLayout llLogisticsInfo;
    @BindView(R.id.rl_refund)
    RelativeLayout rlRefund;
    @BindView(R.id.tx_contacts_name)
    TextView txContactsName;
    @BindView(R.id.tx_contacts_phone)
    TextView txContactsPhone;
    @BindView(R.id.tx_address)
    TextView txAddress;
    @BindView(R.id.tx_order_price)
    TextView txOrderPrice;
    @BindView(R.id.tx_express_name)
    TextView txExpressName;
    @BindView(R.id.tx_express_no)
    TextView txExpressNo;
    @BindView(R.id.tx_express_time)
    TextView txExpressTime;
    @BindView(R.id.tx_dingdanz)
    TextView txDingdanz;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.tx_pay_name)
    TextView txPayName;
    @BindView(R.id.tx_refund_status)
    TextView txRefundStatus;

    private int type;
    private int orderId;
    private String sn;
    private CartContentAdapter adapter;
    private OrderDetailEntity detailEntity;

    public static OrderContentFragment newInstance(int num, int orderId, String sn) {
        OrderContentFragment fragment = new OrderContentFragment();
        Bundle args = new Bundle();
        args.putInt("type", num);
        args.putString(IntentConstant.SN, sn);
        args.putInt(IntentConstant.ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        type = getArguments() != null ? getArguments().getInt("type") : 0;
        sn = getArguments() != null ? getArguments().getString(IntentConstant.SN) : "";
        orderId = getArguments() != null ? getArguments().getInt(IntentConstant.ORDER_ID) : 0;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void aftertView() {
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        presenter.requestDetail(orderId);

        switch (type) {
            case 0:
                btLeftMenu.setText("支付");
                btRightMenu.setVisibility(View.GONE);
                llLogisticsInfo.setVisibility(View.GONE);
                break;

            case 1:
                btLeftMenu.setText("再来一单");
                btRightMenu.setText("退款");
                break;

            case 2:
                btLeftMenu.setText("确认收货");
                btRightMenu.setVisibility(View.GONE);
                break;

            case 3:
                btLeftMenu.setText("再来一单");
                btRightMenu.setText("去评价");
                break;

            case 4:
                btLeftMenu.setText("再来一单");
                btRightMenu.setText("查看退款");
                rlRefund.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_content;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public OrderContentPresenter createPresenter() {
        return new OrderContentPresenter();
    }

    @OnClick({R.id.bt_left_menu, R.id.bt_right_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_left_menu:
                switch (type) {
                    case 0:
                        // 支付
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), PaySelectActivity.class);
                        intent.putExtra(IntentConstant.SN, sn);
                        getActivity().startActivity(intent);
                        break;

                    case 1:
                        // 再来一单
                        buyGoods();
                        break;

                    case 2:
                        // 确认收货
                        if (detailEntity == null) return;
                        presenter.confirmOrder(detailEntity.getOrderId(), detailEntity.getSn());
                        break;

                    case 3:
                        // 再来一单
                        buyGoods();
                        break;

                    case 4:
                        // 再来一单
                        buyGoods();
                        break;
                }

                break;

            case R.id.bt_right_menu:
                Intent intent;
                switch (type) {
                    case 1:
                        // 退款
                        intent = new Intent();
                        intent.setClass(getActivity(), EditRefundActivity.class);
                        intent.putExtra(EditRefundActivity.ORDER_DETAIL_KEY, detailEntity);
                        getActivity().startActivity(intent);
                        break;

                    case 3:
                        // 去评价
                        gotoComment();
                        break;

                    case 4:
                        // 查看退款
                        intent = new Intent();
                        intent.setClass(getActivity(), RefundActivity.class);
                        intent.putExtra(IntentConstant.SN, detailEntity.getSn());
                        intent.putExtra(IntentConstant.ORDER_ID, detailEntity.getOrderId());
                        getActivity().startActivity(intent);
                        break;

                }
                break;
        }
    }

    private void gotoComment() {
        ArrayList<GoodsEntity> goods = (ArrayList<GoodsEntity>) detailEntity.getGoods();
        Intent intent = new Intent();
        intent.setClass(getActivity(), AssessActivity.class);
        intent.putExtra(IntentConstant.GOODS, goods);
        intent.putExtra(IntentConstant.ORDER_ID, Integer.valueOf(detailEntity.getOrderId()));
        getActivity().startActivity(intent);
    }

    @Override
    public void onRequestDetailtSuccess(OrderDetailEntity detailEntity) {
        this.detailEntity = detailEntity;

        ArrayList<GoodsEntity> goods = (ArrayList<GoodsEntity>) detailEntity.getGoods();
        adapter = new CartContentAdapter(getActivity(), goods);
        recyclerView.setAdapter(adapter);

        txOrderPrice.setText(detailEntity.getTotalPrice() + "");
        if (detailEntity.getPayMode().equals("online")) {
            txPayName.setText("在线支付");
        } else {
            txPayName.setText("货到付款");
        }

        String status = detailEntity.getStatus();
        if (status.equals("Refund")) {
            txRefundStatus.setText("退款中");
        } else if (status.equals("Refunded")) {
            txRefundStatus.setText("已退款");
        }

        txAddress.setText(detailEntity.getAddressInfo().getAddress());
        txContactsName.setText(detailEntity.getAddressInfo().getName());
        txContactsPhone.setText(detailEntity.getAddressInfo().getPhone());

        String no = detailEntity.getDistribution().getExpressNo();
        if (!StringUtils.isEmpty(no)) {
            txExpressNo.setText(detailEntity.getDistribution().getExpressNo());
        }
        String name = detailEntity.getDistribution().getExpressName();
        if (!StringUtils.isEmpty(name)) {
            txExpressName.setText(name);
        }
        String time = detailEntity.getDistribution().getExpressTime();
        if (!StringUtils.isEmpty(time)) {
            txExpressTime.setText(time);
        }
    }

    @Override
    public void onRequestDetailtFailure(String message) {
        showToask(message);
    }

    @Override
    public void onConfirmOrderSuccess() {
        EventBus.getDefault().post(new OrderEvent(2));
        EventBus.getDefault().post(new OrderEvent(3));
        gotoComment();
    }

    @Override
    public void onConfirmOrderFailure(String message) {
        showToask(message);
    }

    /**
     * 再来一单
     */
    public void buyGoods() {
        if (detailEntity == null) return;
        Intent intent = new Intent();
        intent.setClass(getActivity(), CartContentActivity.class);
        intent.putParcelableArrayListExtra(CartContentActivity.SELECT_GOODS_KEY, (ArrayList<? extends Parcelable>) detailEntity.getGoods());
        getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
