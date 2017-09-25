package com.tiger.socol.gu.activity.mine.collection;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.blankj.utilcode.utils.ToastUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.product.content.ProductContentActivity;
import com.tiger.socol.gu.api.good.CollectApi;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;

public class CollectionActivity extends BaseViewStateActivity<CollectionView, CollectionPresenter>
        implements CollectionView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    SwipeMenuListView listView;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;

    private CollectionAdapter adapter;

    @NonNull
    @Override
    public CollectionPresenter createPresenter() {
        return new CollectionPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("收藏");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CollectionAdapter(this);
        listView.setAdapter(adapter);


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.RED));
                openItem.setWidth(DensityUtil.dip2px(CollectionActivity.this, 90));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        collectGood(position);
                        break;
                }
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(CollectionActivity.this, ProductContentActivity.class);
                intent.putExtra(ProductContentActivity.PRODUCT_ID_KEY, adapter.getGoods().get(i).getGoodsId());
                CollectionActivity.this.startActivity(intent);
            }
        });

        showProgress();
        presenter.requestFrilstPage();
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestFrilstPage();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestNextPage();
            }
        });
    }

    @Override
    public void onRequestFristPageSuccess(List<GoodsEntity> goods, boolean more) {
        dismissProgress();
        adapter.setGoods(goods);
        refresh.setLoadMore(more);
        refresh.finishRefresh();
    }

    private void reuqestFailure(String message) {
        dismissProgress();
        showToask(message);
        refresh.finishRefresh();
        refresh.finishRefreshLoadMore();
    }

    @Override
    public void onRequestFristPageFailure(String message) {
        reuqestFailure(message);
    }

    @Override
    public void onRequestNextPageSuccess(List<GoodsEntity> goods, boolean more) {
        adapter.setGoods(goods);
        refresh.setLoadMore(more);
        refresh.finishRefreshLoadMore();
    }

    @Override
    public void onRequestNextPageFailure(String message) {
        reuqestFailure(message);
    }

    public void collectGood(final int position) {
        final GoodsEntity entity = adapter.getGoods().get(position);
        CollectApi api = new CollectApi();
        api.goodsId = entity.getGoodsId();
        api.collect = false;
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                adapter.delete(entity);
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showShortToast(CollectionActivity.this, "操作失败");
            }
        });
    }

}

