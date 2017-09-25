package com.tiger.socol.gu.activity.community;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.community.Content.CommentDialog;
import com.tiger.socol.gu.activity.community.Content.CommunityContentActivity;
import com.tiger.socol.gu.activity.community.add.AddCommunityActivity;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.api.bbs.AdEntity;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.base.BaseViewStateFragment;
import com.tiger.socol.gu.managers.MemberMannager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

public class CommunityFragment extends BaseViewStateFragment<CommunityView, CommunityPresenter>
        implements CommunityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;

    private List<BBSEntity> flow;
    private CircleAdapter adapter;

    @NonNull
    @Override
    public CommunityPresenter createPresenter() {
        return new CommunityPresenter();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void aftertView() {
        toolbar.setTitle("食为天");
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager glm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(glm);
        adapter = new CircleAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestFristPage();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                presenter.requestNextPage();
            }
        });
        refresh.autoRefresh();

        presenter.requestAds();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_community;
    }

    @Override
    protected void initView(View view) {
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bt_add_community: {
                startActi(AddCommunityActivity.class);
                break;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        inflater.inflate(R.menu.menu_add_community, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRequestAdsSuccess(List<AdEntity> ads) {
        adapter.setAds(ads);
    }

    @Override
    public void onRequestAdsFailure(String message) {

    }

    @Override
    public void onRequestFirstBBSListSuccess(List<BBSEntity> flow, boolean more) {
        this.flow = flow;
        adapter.setBBSList(flow);
        refresh.setLoadMore(more);
        refresh.finishRefresh();
    }

    @Override
    public void onRequestFirstBBSListFailure(String message) {
        reuqestFailure(message);
    }

    private void reuqestFailure(String message) {
        showToask(message);
        refresh.finishRefresh();
        refresh.finishRefreshLoadMore();
    }

    @Override
    public void onRequestNextBBSListSuccess(List<BBSEntity> flow, boolean more) {
        this.flow = flow;
        adapter.setBBSList(flow);
        refresh.setLoadMore(more);
        refresh.finishRefreshLoadMore();
    }

    @Override
    public void onRequestNextBBSListFailure(String message) {
        reuqestFailure(message);
    }

}