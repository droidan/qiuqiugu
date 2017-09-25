package com.tiger.socol.gu.activity.product;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.product.search.SearchActivity;
import com.tiger.socol.gu.api.index.FarmEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.base.BaseViewStateFragment;

import java.util.List;

import butterknife.BindView;

public class ProductFragment extends BaseViewStateFragment<ProductView, ProductPresenter>
        implements ProductView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;

    private int offset = 0;
    private int startOffset = 0;
    private int endOffset = 0;

    private DemoAdapter adapter;

    @NonNull
    @Override
    public ProductPresenter createPresenter() {
        return new ProductPresenter();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void aftertView() {
        toolbar.getBackground().setAlpha(0);//toolbar透明度初始化为0
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActi(SearchActivity.class);
            }
        });

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new DemoAdapter(getActivity(), true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration());

        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestFristPage();
                presenter.farmList();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestNextPage();
            }
        });
        refresh.autoRefresh();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                startOffset = 0;
                endOffset = slideViewHight() - toolbar.getHeight();
                offset += dy;
                if (offset <= startOffset) {  //alpha为0
                    toolbar.getBackground().setAlpha(0);
                } else if (offset > startOffset && offset < endOffset) { //alpha为0到255
                    float precent = (float) (offset - startOffset) / endOffset;
                    int alpha = Math.round(precent * 255);
                    toolbar.getBackground().setAlpha(alpha);
                } else if (offset >= endOffset) {  //alpha为255
                    toolbar.getBackground().setAlpha(255);
                }
            }
        });
    }

    private int slideViewHight() {
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getRealSize(point);
        int height = point.x / 16 * 9;
        return height;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {

                break;
            }
        }
        return true;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onRequestFristPageSuccess(List<GoodsEntity> goods, boolean more) {
        adapter.setGoods(goods);
        refresh.setLoadMore(more);
        refresh.finishRefresh();
    }

    private void reuqestFailure(String message) {
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

    @Override
    public void onRequestFarmSuccess(List<FarmEntity> farms) {
        adapter.setFarms(farms);
    }

    @Override
    public void onRequestFarmFailure(String message) {

    }

}