package com.tiger.socol.gu.activity.mine.order.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.activity.mine.order.OrderEvent;
import com.tiger.socol.gu.activity.shopCart.root.CartEvent;
import com.tiger.socol.gu.api.order.OrderDetailEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.views.widgets.LineTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderContentActivity extends BaseViewStateActivity<OrderContentView, OrderContentPresenter>
        implements OrderContentView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tx_title_one)
    LineTextView txTitleOne;
    @BindView(R.id.tx_title_four)
    LineTextView txTitleFour;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private int type = 0;
    private int orderId = 0;
    private String sn;
    public static final String PAGE_INDEX = "pageindex";
    public static final String TYPE = "type";

    private OrderContentFragment orderContentFragment;
    private CartEvent event;

    @NonNull
    @Override
    public OrderContentPresenter createPresenter() {
        return new OrderContentPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_content;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txTitleOne.setText("订单详情");
        txTitleFour.setText("订单跟踪");
        txTitleOne.setTextSize(17);
        txTitleFour.setTextSize(17);

        type = getIntent().getIntExtra(TYPE, 0);
        sn = getIntent().getStringExtra(IntentConstant.SN);
        orderId = getIntent().getIntExtra(IntentConstant.ORDER_ID, 0);

        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return OrderContentFragment.newInstance(type, orderId, sn);
                } else {
                    return OrderLogisticsFragment.newInstance(orderId);
                }
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int pageIndex = getIntent().getIntExtra(PAGE_INDEX, 0);
        changeFragment(pageIndex);
        viewpager.setCurrentItem(pageIndex);
    }

    @OnClick({R.id.rl_order_one, R.id.rl_order_four})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_order_one:
                changeFragment(0);
                viewpager.setCurrentItem(0);
                break;
            case R.id.rl_order_four:
                changeFragment(1);
                viewpager.setCurrentItem(1);
                break;
        }
    }

    private void changeFragment(int position) {
        if (position == 0) {
            txTitleOne.setSelected(true);
            txTitleFour.setSelected(false);
        } else if (position == 1) {
            txTitleOne.setSelected(false);
            txTitleFour.setSelected(true);
        }
    }

    @Subscribe
    public void onEventMainThread(OrderEvent event) {
        // 评价完成
        if (event.getType() == 3) {
            finish();
        } else if (event.getType() == 0) {
            // 支付完成
            finish();
        } else if (event.getType() == 1) {
            // 款款
            finish();
        } else if (event.getType() == 2) {
            // 确认收货
            finish();
        }
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

    @Override
    public void onRequestDetailtSuccess(OrderDetailEntity detailEntity) {

    }

    @Override
    public void onRequestDetailtFailure(String message) {

    }

    @Override
    public void onConfirmOrderSuccess() {

    }

    @Override
    public void onConfirmOrderFailure(String message) {

    }

    @Subscribe
    public void onEventMainThread(CartEvent event) {
        finish();
    }
}