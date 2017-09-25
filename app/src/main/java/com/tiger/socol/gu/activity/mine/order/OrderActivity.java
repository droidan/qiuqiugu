package com.tiger.socol.gu.activity.mine.order;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.base.BaseActivity;
import com.tiger.socol.gu.views.widgets.LineTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.tx_title_one)
    LineTextView txTitleOne;
    @BindView(R.id.tx_title_two)
    LineTextView txTitleTwo;
    @BindView(R.id.tx_title_three)
    LineTextView txTitleThree;
    @BindView(R.id.tx_title_four)
    LineTextView txTitleFour;
    @BindView(R.id.tx_title_five)
    LineTextView txTitleFive;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static final String ORDER_TYPE_KEY = "OrderTypeKey";

    private int orderTypeIndex = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        orderTypeIndex = getIntent().getIntExtra(ORDER_TYPE_KEY, 0);
        selectOrderType(orderTypeIndex);

        txTitleOne.setText("待支付");
        txTitleTwo.setText("待发货");
        txTitleThree.setText("待收货");
        txTitleFour.setText("待评价");
        txTitleFive.setText("退款/退货");

        toolbar.setTitle("我的订单");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewpager.setAdapter(new OrderPageAdapter(getSupportFragmentManager()));
        viewpager.setCurrentItem(orderTypeIndex);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectOrderType(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.rl_order_one, R.id.rl_order_two, R.id.rl_order_three, R.id.rl_order_four, R.id.rl_order_five})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_order_one:
                selectOrderType(0);
                break;

            case R.id.rl_order_two:
                selectOrderType(1);
                break;

            case R.id.rl_order_three:
                selectOrderType(2);
                break;

            case R.id.rl_order_four:
                selectOrderType(3);
                break;

            case R.id.rl_order_five:
                selectOrderType(4);
                break;
        }
    }

    /**
     * 选择订单类型
     *
     * @param index index
     */
    private void selectOrderType(int index) {
        viewpager.setCurrentItem(index);
        switch (index) {
            case 0:
                txTitleOne.setSelected(true);
                txTitleTwo.setSelected(false);
                txTitleThree.setSelected(false);
                txTitleFour.setSelected(false);
                txTitleFive.setSelected(false);
                break;

            case 1:
                txTitleOne.setSelected(false);
                txTitleTwo.setSelected(true);
                txTitleThree.setSelected(false);
                txTitleFour.setSelected(false);
                txTitleFive.setSelected(false);
                break;

            case 2:
                txTitleOne.setSelected(false);
                txTitleTwo.setSelected(false);
                txTitleThree.setSelected(true);
                txTitleFour.setSelected(false);
                txTitleFive.setSelected(false);
                break;

            case 3:
                txTitleOne.setSelected(false);
                txTitleTwo.setSelected(false);
                txTitleThree.setSelected(false);
                txTitleFour.setSelected(true);
                txTitleFive.setSelected(false);
                break;

            case 4:
                txTitleOne.setSelected(false);
                txTitleTwo.setSelected(false);
                txTitleThree.setSelected(false);
                txTitleFour.setSelected(false);
                txTitleFive.setSelected(true);
                break;
        }
    }

}
