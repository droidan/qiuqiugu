package com.tiger.socol.gu.activity.community.Content;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.managers.MemberMannager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CommunityContentActivity extends BaseViewStateActivity<CommunityContentView, CommunityContentPresenter>
        implements CommunityContentView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;

    private CommunityContentAdapter adapter;
    private CommentDialog dialog;

    public static final String BBS_ENTITY_KEY = "BBS_ENTITY_KEY";
    private BBSEntity bbsEntity;

    @NonNull
    @Override
    public CommunityContentPresenter createPresenter() {
        return new CommunityContentPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_community_content;
    }

    @Override
    protected void initData() {
        bbsEntity = getIntent().getParcelableExtra(BBS_ENTITY_KEY);
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("评论");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager glm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(glm);
        adapter = new CommunityContentAdapter(this);
        adapter.setEntity(bbsEntity);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.setAdapter(adapter);

        adapter.setReplyListener(new CommunityContentAdapter.OnCommentItemReplyListener() {
            @Override
            public void onReplyCleck(final int commentId) {
                if (!MemberMannager.checkLogin(CommunityContentActivity.this)) {
                    return;
                }
                dialog = new CommentDialog(CommunityContentActivity.this);
                dialog.setLislener(new CommentDialog.CommentDialogLislener() {
                    @Override
                    public void subimtComment(String content) {
                        presenter.reply(commentId, content);
                    }
                });
                dialog.show();
            }
        });

        presenter.requestFristPageComment(bbsEntity.getBbsId());
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestFristPageComment(bbsEntity.getBbsId());
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestNextPageComment(bbsEntity.getBbsId());
            }
        });
    }

    @OnClick(R.id.bt_submit_comment)
    public void onClick() {
        if (!MemberMannager.checkLogin(CommunityContentActivity.this)) {
            return;
        }
        dialog = new CommentDialog(this);
        dialog.setLislener(new CommentDialog.CommentDialogLislener() {
            @Override
            public void subimtComment(String content) {
                presenter.submit(content, bbsEntity.getBbsId());
            }
        });
        dialog.show();
    }

    private void showCommitDialog() {

    }

    @Override
    public void onSubmitSuccess(CommentEntity commentEntity) {
        adapter.installComment(commentEntity);
    }

    @Override
    public void onSubmitFailure(String message) {
        showToask("评论失败");
    }

    private void reuqestFailure() {
        showToask("加载评论失败");
        refresh.finishRefresh();
        refresh.finishRefreshLoadMore();
    }

    private void reuqestSuccess(List<CommentEntity> comments, boolean more) {
        adapter.setCommentEntityList(comments);
        refresh.setLoadMore(more);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onRequestFristPageCommentSuccess(List<CommentEntity> comments, boolean more) {
        reuqestSuccess(comments, more);
        refresh.finishRefresh();
    }

    @Override
    public void onRequestNextPageCommentSuccess(List<CommentEntity> comments, boolean more) {
        reuqestSuccess(comments, more);
        refresh.finishRefreshLoadMore();
    }

    @Override
    public void onRequestNextPageCommentFailure(String message) {
        reuqestFailure();
    }

    @Override
    public void onRequestFristPageCommentFailure(String message) {
        reuqestFailure();
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int count = parent.getAdapter().getItemCount();
            int position = parent.getChildAdapterPosition(view);

            if (position == 1) {
                outRect.top = 10;
            } else {
                outRect.top = 0;
            }

            if (position == count - 1) {
                outRect.bottom = 10;
            } else {
                outRect.bottom = 0;
            }

            outRect.right = 0;
            outRect.left = 0;
        }
    }

}

