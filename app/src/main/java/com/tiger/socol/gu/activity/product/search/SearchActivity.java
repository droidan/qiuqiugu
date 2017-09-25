package com.tiger.socol.gu.activity.product.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.product.DemoAdapter;
import com.tiger.socol.gu.activity.product.SpacesItemDecoration;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.managers.KeywordMannager;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseViewStateActivity<SearchAView, SearchPresenter>
        implements SearchAView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_hot)
    TagFlowLayout flHot;
    @BindView(R.id.ll_hot)
    LinearLayout llHot;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.rv_good)
    RecyclerView rvGood;
    @BindView(R.id.sc_history)
    ScrollView llHistory;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.ll_history_men)
    LinearLayout llHistoryMen;
    @BindView(R.id.myEditText)
    EditText myEditText;
    @BindView(R.id.bt_cancle)
    Button btCancle;
    @BindView(R.id.bt_delete)
    ImageView btDelete;

    private LayoutInflater mInflater;
    private SearchAdapter adapter;
    private DemoAdapter goodAdapter;
    private String keyword;

    @NonNull
    @Override
    public SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        presenter.requestKeywords();

        mInflater = LayoutInflater.from(this);

        rvHistory.setNestedScrollingEnabled(false);
        LinearLayoutManager glm = new LinearLayoutManager(this);
        rvHistory.setLayoutManager(glm);
        adapter = new SearchAdapter(this);
        rvHistory.setAdapter(adapter);
        adapter.setListener(new SearchAdapter.OnSearchAdapterListener() {
            @Override
            public void onItemClick(String keyword) {
                SearchActivity.this.keyword = keyword;
                presenter.searchFisrt(keyword);
            }

            @Override
            public void onItemClean() {
                llHistoryMen.setVisibility(View.GONE);
            }
        });

        List<String> history = KeywordMannager.getInstance().list();
        if (history.size() == 0) {
            llHistoryMen.setVisibility(View.GONE);
        } else {
            llHistoryMen.setVisibility(View.VISIBLE);
        }
        adapter.setHistorys(history);

        goodAdapter = new DemoAdapter(this, false);
        rvGood.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvGood.addItemDecoration(new SpacesItemDecoration());
        rvGood.setAdapter(goodAdapter);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                presenter.searchFisrt(keyword);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                presenter.searchNext(keyword);
            }
        });

        myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    btCancle.setText("取消");
                    btDelete.setVisibility(View.GONE);
                } else {
                    btCancle.setText("搜索");
                    btDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText("");
            }
        });
    }

    @Override
    public void searchSuccess(List<GoodsEntity> goods, boolean more) {
        if (goods.size() == 0) {
            showToask("没有相关产品");
            return;
        }
        goodAdapter.setGoods(goods);
        refresh.setLoadMore(more);
        refresh.finishRefresh();
        refresh.finishRefreshLoadMore();
        llHistory.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void searchFailure(String message) {
        showToask(message);
    }

    @Override
    public void requestKeywordsSuccess(final List<String> keywords) {
        if (keywords.size() == 0) return;
        llHot.setVisibility(View.VISIBLE);

        flHot.setAdapter(new TagAdapter<String>(keywords) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.view_tx,
                        flHot, false);
                tv.setText(s);
                return tv;
            }
        });
        flHot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                SearchActivity.this.keyword = keywords.get(position);
                KeywordMannager.getInstance().add(keyword);
                presenter.searchFisrt(keyword);
                return true;
            }
        });
    }

    @OnClick({R.id.bt_cancle, R.id.bt_clean})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancle:
                String keyword = myEditText.getText().toString();
                if (StringUtils.isEmpty(keyword)) {
                    finish();
                } else {
                    this.keyword = keyword;
                    KeywordMannager.getInstance().add(keyword);
                    presenter.searchFisrt(keyword);
                }
                break;

            case R.id.bt_clean:
                KeywordMannager.getInstance().clean();
                llHistoryMen.setVisibility(View.GONE);
                break;
        }
    }

}
