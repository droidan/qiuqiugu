package com.tiger.socol.gu.activity.mine.order.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.activity.mine.order.OrderEvent;
import com.tiger.socol.gu.api.order.OrderDetailEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditRefundActivity extends BaseViewStateActivity<EditRefundView, EditRefundPresenter>
        implements EditRefundView {

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
    @BindView(R.id.ed_content)
    EditText edContent;

    public static final String ORDER_DETAIL_KEY = "ORDER_DETAIL_KEY";
    private OrderDetailEntity detailEntity;

    @NonNull
    @Override
    public EditRefundPresenter createPresenter() {
        return new EditRefundPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_refund;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("退款");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() != R.id.menu_bt_submit) {
                    return true;
                }

                String content = edContent.getText().toString();
                if (StringUtils.isEmpty(content)) {
                    showToask("请输入退款原因");
                    return true;
                }

                presenter.refund(detailEntity.getOrderId(), detailEntity.getSn(), content);
                return true;
            }
        });

        detailEntity = getIntent().getParcelableExtra(ORDER_DETAIL_KEY);
        txProductTime.setText(detailEntity.getCreated());
        txPorderTitle.setText(detailEntity.getGoods().get(0).getGoodsName());
        txProductNum.setText("共计" + detailEntity.getGoods().size() + "件商品");
        String thumb = detailEntity.getGoods().get(0).getThumb();
        if (!StringUtils.isEmpty(thumb)) {
            Picasso.with(this).load(thumb).into(imProductThumb);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public void onRefundSuccess(String message) {
        showToask(message);
        finish();
        EventBus.getDefault().post(new OrderEvent(1));
        EventBus.getDefault().post(new OrderEvent(4));
    }

    @Override
    public void onRefundFailure(String message) {
        showToask(message);
    }

}
