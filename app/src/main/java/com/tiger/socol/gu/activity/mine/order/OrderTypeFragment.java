package com.tiger.socol.gu.activity.mine.order;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.api.order.OrderListEntity;
import com.tiger.socol.gu.base.BaseViewStateFragment;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

public class OrderTypeFragment extends BaseViewStateFragment<OrderTypeView, OrderTypePresenter>
        implements OrderTypeView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;

    private int type;
    private int orderId;
    private OrderAdapter adapter;

    private boolean requestSuccess = false;

    public static OrderTypeFragment newInstance(int num) {
        OrderTypeFragment fragment = new OrderTypeFragment();
        Bundle args = new Bundle();
        args.putInt("type", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        type = getArguments() != null ? getArguments().getInt("type") : 0;
        orderId = getArguments() != null ? getArguments().getInt(IntentConstant.ORDER_ID) : 0;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(OrderEvent event) {
        if (event.getType() == type) {
            presenter.requestFirstPage(getOrderStatus());
        }
    }

    @Override
    protected void aftertView() {
        if (requestSuccess) return;
        presenter.requestFirstPage(getOrderStatus());
    }

    private String getOrderStatus() {
        String orderStatus = "";
        switch (type) {
            case 0:
                orderStatus = "Unpaid";
                break;

            case 1:
                orderStatus = "Pending";
                break;

            case 2:
                orderStatus = "Dispatch";
                break;

            case 3:
                orderStatus = "Complete";
                break;

            case 4:
                orderStatus = "Refund";
                break;
        }
        return orderStatus;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_type_list;
    }

    @Override
    protected void initView(View view) {
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager glm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(glm);

        adapter = new OrderAdapter(type, getActivity());
        recyclerView.addItemDecoration(new OrderTypeFragment.OrderItemDecoration());
        recyclerView.setAdapter(adapter);

        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestFirstPage(getOrderStatus());
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestNextPage(getOrderStatus());
            }
        });
    }

    @Override
    public OrderTypePresenter createPresenter() {
        return new OrderTypePresenter();
    }

    public void onRequestSuccess(List<OrderListEntity> orders, boolean more) {
        adapter.setOrders(orders);
        refresh.setLoadMore(more);
        refresh.finishRefresh();
        refresh.finishRefreshLoadMore();
    }
    @Override
    public void onRequestFistPageSuccess(List<OrderListEntity> orders, boolean more) {
        requestSuccess = true;
        onRequestSuccess(orders, more);
    }

    @Override
    public void onRequestNextPageSuccess(List<OrderListEntity> orders, boolean more) {
        onRequestSuccess(orders, more);
    }

    private void reuqestFailure(String message) {
        showToask(message);
        refresh.finishRefresh();
        refresh.finishRefreshLoadMore();
    }

    @Override
    public void onRequestFistPageFailure(String message) {
        reuqestFailure(message);
    }

    @Override
    public void onRequestNextPageFailure(String message) {
        reuqestFailure(message);
    }

    public class OrderItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                outRect.top = 20;
            } else {
                outRect.top = 0;
            }

            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = 20;
        }
    }

}

