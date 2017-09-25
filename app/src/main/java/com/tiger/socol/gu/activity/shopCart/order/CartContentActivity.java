package com.tiger.socol.gu.activity.shopCart.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.address.list.AddressListActivity;
import com.tiger.socol.gu.activity.shopCart.paySelect.PayDialog;
import com.tiger.socol.gu.activity.shopCart.paySelect.PaySelectActivity;
import com.tiger.socol.gu.activity.shopCart.root.CartEvent;
import com.tiger.socol.gu.api.address.AddressEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.api.menber.VoucherEntity;
import com.tiger.socol.gu.api.order.ExpressCostEntity;
import com.tiger.socol.gu.api.order.OrderEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.utils.DoubleUtils;
import com.tiger.socol.gu.views.widgets.FullyLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CartContentActivity extends BaseViewStateActivity<CartContentView, CartContentPresenter>
        implements CartContentView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tx_pay_name)
    TextView txPayName;
    @BindView(R.id.tx_address_name)
    TextView txAddressName;
    @BindView(R.id.tx_address_phone)
    TextView txAddressPhone;
    @BindView(R.id.tx_address_info)
    TextView txAddressInfo;
    @BindView(R.id.im_defult)
    ImageView imDefult;

    public static final int PAY_REQUEST_CODE = 199;
    public static final int ADDRESS_RESULT_CODE = 200;
    public static final String SELECT_GOODS_KEY = "SELECT_GOODS_KEY";
    @BindView(R.id.tx_price)
    TextView txPrice;
    @BindView(R.id.ed_postage)
    EditText edPostage;
    @BindView(R.id.tv_defult)
    TextView tvDefult;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;

    private int payType = 0;
    private double goodsPrice = 0;
    private double voucherPrice = 0;
    private double deliveryPrice = 0;
    private AddressEntity addressEntity;
    private CartContentAdapter adapter;
    private ArrayList<GoodsEntity> goods;
    private ExpressCostEntity costEntity;
    private List<VoucherEntity> vouchers = new ArrayList<>();

    @NonNull
    @Override
    public CartContentPresenter createPresenter() {
        return new CartContentPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart_content;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("填写信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goods = getIntent().getParcelableArrayListExtra(SELECT_GOODS_KEY);

        goodsPrice = getGoodsPrice();
        edPostage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    return;
                }
                voucherPrice = getVoucherMoney(vouchers);
            }
        });

        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CartContentAdapter(this, goods);
        recyclerView.setAdapter(adapter);

        showProgress();
        presenter.requestAddress();
        presenter.requestPostage();
        presenter.requestVoucherList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ADDRESS_RESULT_CODE:
                Bundle bb = data.getExtras();
                addressEntity = bb.getParcelable(IntentConstant.ADDRESS);
                setAddress(addressEntity);
                break;

            default:
                break;
        }
    }

    @OnClick({R.id.rl_order_address, R.id.rl_pay_type, R.id.bt_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_order_address:
                // 选择收货地址
                Intent intent = new Intent();
                intent.setClass(this, AddressListActivity.class);
                intent.putExtra(AddressListActivity.SELECT_ADDRESS_KEY, true);
                startActivityForResult(intent, ADDRESS_RESULT_CODE);
                break;

            case R.id.rl_pay_type:
                // 支付方式
                PayDialog dialog = new PayDialog(this);
                dialog.setListeren(new PayDialog.OnPaySelectListeren() {
                    @Override
                    public void onItemClick(int position) {
                        payType = position;
                        if (payType == 0) {
                            txPayName.setText("在线支付");
                        } else {
                            txPayName.setText("货到付款");
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.bt_submit:
                // 支付
                submint();
                break;
        }
    }

    private void submint() {
        if (addressEntity == null) {
            showToask("请选择收货地址");
            return;
        }

        if (costEntity == null) {
            return;
        }
        showProgress();
        String payMode = payType == 0 ? "online" : "delivery";
        double totlePrice = goodsPrice + deliveryPrice - voucherPrice;
        presenter.submit(addressEntity.getAddressId(), goods, payMode, null, totlePrice, goodsPrice, deliveryPrice);
    }

    /**
     * 设置收件人地址
     *
     * @param entity 地址
     */
    private void setAddress(AddressEntity entity) {
        tvDefult.setVisibility(View.GONE);
        imDefult.setVisibility(View.GONE);
        rlAddress.setVisibility(View.VISIBLE);
        addressEntity = entity;
        txAddressName.setText(entity.getName());
        txAddressPhone.setText(entity.getPhone());
        txAddressInfo.setText(entity.getProvince() + entity.getCity() + entity.getArea() + entity.getAddress());
    }

    /**
     * 获取所有产品费用
     *
     * @return price
     */
    private double getGoodsPrice() {
        double price = 0;
        for (GoodsEntity good : goods) {
            double p = (double) good.getNum() * good.getPrice();
            price += p;
        }
        return DoubleUtils.toPrice(price);
    }

    /**
     * 获取邮费
     *
     * @return price
     */
    private double getDeliveryPrice(ExpressCostEntity entity) {
        double weight = 0;
        for (GoodsEntity good : goods) {
            weight += good.getExpressWeight() * good.getNum();
        }
        double price = entity.price(weight);
        return price;
    }

    /**
     * 获取优惠券金额
     */
    private double getVoucherMoney(List<VoucherEntity> vouchers) {
        String s = edPostage.getText().toString();
        if (StringUtils.isEmpty(s)) {
            return 0;
        }
        for (VoucherEntity voucher : vouchers) {
            if (voucher.getVoucher().equals(s)) {
                return voucher.getMoney();
            }
        }

        return 0;
    }

    @Override
    public void onRequestAddressSuccess(AddressEntity address) {
        setAddress(address);
    }

    @Override
    public void onRequestAddressFailure(String message) {

    }

    @Override
    public void onRequestVoucherListSuccess(List<VoucherEntity> vouchers) {
        this.vouchers = vouchers;
        voucherPrice = getVoucherMoney(vouchers);
    }

    @Override
    public void onRequestVoucherListFailure(String message) {

    }

    @Override
    public void onRequestPostageSuccess(ExpressCostEntity entity) {
        dismissProgress();
        costEntity = entity;
        deliveryPrice = getDeliveryPrice(entity);
        double price = DoubleUtils.toPrice(goodsPrice + deliveryPrice);
        txPrice.setText("￥" + price);
    }

    @Override
    public void onRequestPostageFailure(String message) {
        dismissProgress();
    }

    @Override
    public void orderPaySuccess(OrderEntity orderEntity) {
        dismissProgress();
        if (payType == 0) {
            Intent intent = new Intent();
            intent.setClass(this, PaySelectActivity.class);
            intent.putExtra(IntentConstant.SN, orderEntity.getSn());
            startActivity(intent);
        } else {
            showToask("支付成功");
        }
        EventBus.getDefault().post(new CartEvent());
    }

    @Override
    public void orderPayFailure(String message) {
        dismissProgress();
        showToask("支付失败");
    }

    @Subscribe
    public void onEventMainThread(CartEvent event) {
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

}

