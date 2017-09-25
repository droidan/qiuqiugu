package com.tiger.socol.gu.activity.product.content;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.good.GoodsCommentEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CommentListActivity extends BaseViewStateActivity<CommentListView, CommentListPresenter>
        implements CommentListView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tx_porder_title)
    TextView txPorderTitle;
    @BindView(R.id.im_product_thumb)
    ImageView imProductThumb;
    @BindView(R.id.recyclerView)
    RecyclerView rvComment;
    @BindView(R.id.mb_start)
    MaterialRatingBar mbStart;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;

    private int start;
    private int goodId;
    private String name;
    private String thumbs;
    private CommentAdapter commentAdapter;

    public static final String GOOD_NAME_KEY = "GOOD_NAME_KEY";
    public static final String GOOD_START_KEY = "GOOD_START_KEY";
    public static final String GOOD_THUMBS_KEY = "GOOD_THUMBS_KEY";

    @NonNull
    @Override
    public CommentListPresenter createPresenter() {
        return new CommentListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("评价");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComment.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(true);
        rvComment.setAdapter(commentAdapter);
        rvComment.addItemDecoration(new SpacesItemDecoration());

        name = getIntent().getStringExtra(GOOD_NAME_KEY);
        start = getIntent().getIntExtra(GOOD_START_KEY, 5);
        thumbs = getIntent().getStringExtra(GOOD_THUMBS_KEY);
        goodId = getIntent().getIntExtra(IntentConstant.GOOD_ID, 0);

        mbStart.setNumStars(5);
        mbStart.setRating(start);
        txPorderTitle.setText(name);
        if (!StringUtils.isEmpty(thumbs)) {
            Picasso.with(this).load(thumbs).into(imProductThumb);
        }

        presenter.requestFristPage(goodId);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestFristPage(goodId);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestNextPage(goodId);
            }
        });
    }

    @Override
    public void onRequestFristPageSuccess(List<GoodsCommentEntity> comments, boolean more) {
        commentAdapter.setComments(comments);
        refresh.setLoadMore(more);
        refresh.finishRefresh();
    }

    @Override
    public void onRequestNextPageSuccess(List<GoodsCommentEntity> comments, boolean more) {
        commentAdapter.setComments(comments);
        refresh.setLoadMore(more);
        refresh.finishRefreshLoadMore();
    }

    private void reuqestFailure(String message) {
        showToask(message);
        refresh.finishRefresh();
        refresh.finishRefreshLoadMore();
    }

    @Override
    public void onRequestNextPageFailure(String message) {
        reuqestFailure(message);
    }

    @Override
    public void onRequestFristPageFailure(String message) {
        reuqestFailure(message);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        public SpacesItemDecoration() {
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.right = 0;
            outRect.bottom = 20;
            outRect.top = 0;
            outRect.left = 0;
        }

    }
}
