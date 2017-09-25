package com.tiger.socol.gu.activity.product.content;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.medialibrary.video.JCVideoPlayer;
import com.medialibrary.video.listener.ScreenListener;
import com.medialibrary.video.utils.ScreenSwitchUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.product.DemoAdapter;
import com.tiger.socol.gu.activity.product.SpacesItemDecoration;
import com.tiger.socol.gu.api.farm.FarmDetailEntity;
import com.tiger.socol.gu.api.good.VideoEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.network.Config;
import com.tiger.socol.gu.views.widgets.CustomWebView;
import com.tiger.socol.gu.views.widgets.VideoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FarmContentActivity extends BaseViewStateActivity<FarmContentView, FarmContentPresenter>
        implements FarmContentView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tx_fram_name)
    TextView txFramName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.wb_fram_conten)
    CustomWebView wbFramContent;
    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @BindView(R.id.custom_videoplayer)
    VideoPlayerView viewDetailLesson;
    @BindView(R.id.im_thumb)
    ImageView imThumb;
    @BindView(R.id.im_play)
    ImageView imPlay;
    @BindView(R.id.rl_thumb)
    RelativeLayout rlThumb;

    private FarmProductAdapter adapter;
    private FarmDetailEntity farmDetailEntity;
    private DemoAdapter demoAdapter;
    private int baseId;

    public static final String FARM_CONTENT_ID_KEY = "FARM_CONTENT_KEY";

    @NonNull
    @Override
    public FarmContentPresenter createPresenter() {
        return new FarmContentPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_farm_content;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        requestContent();
    }

    private void requestContent() {
        showProgress();
        baseId = getIntent().getIntExtra(FARM_CONTENT_ID_KEY, 0);
        presenter.requestContent(baseId);
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        ViewGroup.LayoutParams params = rlThumb.getLayoutParams();
        params.width = point.x;
        int height = params.width / 16 * 9;
        params.height = height;
        rlThumb.setLayoutParams(params);

        wbFramContent.getSettings().setJavaScriptEnabled(true);
        wbFramContent.getSettings().setAllowFileAccess(true);
        wbFramContent.getSettings().setSupportMultipleWindows(true);
        wbFramContent.getSettings().setJavaScriptEnabled(true);
        wbFramContent.getSettings().setDomStorageEnabled(true);
        wbFramContent.getSettings().setBlockNetworkImage(false);
        wbFramContent.getSettings().setDisplayZoomControls(false);
        wbFramContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wbFramContent.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wbFramContent.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wbFramContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            wbFramContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        viewDetailLesson.setScreenListener(screenListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter = new FarmProductAdapter(this));

        // 相关产品
        rvRecommend.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        demoAdapter = new DemoAdapter(this, false);
        rvRecommend.setAdapter(demoAdapter);
        rvRecommend.setNestedScrollingEnabled(false);
        rvRecommend.addItemDecoration(new SpacesItemDecoration());

        requestContent();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_submit, menu);
//        return true;
//    }

    @Override
    public void onRequestContentFailure(String message) {
        dismissProgress();
        showToask(message);
    }

    @Override
    public void onRequestContentSuccess(FarmDetailEntity entity) {
        dismissProgress();
        farmDetailEntity = entity;
        txFramName.setText(entity.getBaseName());
        wbFramContent.loadWebDataBaseURL(Config.URL, entity.getContent());
        adapter.setGoodList(entity.getGoods().getData());
        demoAdapter.setGoods(entity.getRelated().getData());

        // 设置封面
        String live = entity.getLiveUrl();
        if (StringUtils.isEmpty(live)) {
            imPlay.setVisibility(View.GONE);
        } else {
            imPlay.setVisibility(View.VISIBLE);
        }
        Picasso.with(this).load(entity.getThumb()).into(imThumb);

        initVideoData(entity.getLive());
    }

    // -------------------------------视频---------------------------------------------

    private void initVideoData(VideoEntity videoInfo) {
        viewDetailLesson.setVideoInfo(videoInfo);
    }


    private ScreenSwitchUtils instance;

    /**
     * 播放器监听
     */
    private ScreenListener screenListener = new ScreenListener() {
        @Override
        public void onFullscreenOnclicker() {
            if (instance == null)
                return;
            instance.toggleScreen();
        }

        @Override
        public void onCompletionListener() {
        }

        @Override
        public void onPreparedListener() {
            if (instance == null)
                instance = new ScreenSwitchUtils(FarmContentActivity.this);
            instance.start(FarmContentActivity.this);
        }
    };

    /**
     * 释放媒体对象
     */
    public void releaseMediaPlayer() {
        viewDetailLesson.releaseMediaPlayer();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewDetailLesson.setLandscape(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 切换成竖屏
            viewDetailLesson.setLandscape(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewDetailLesson.setMideapalyerResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewDetailLesson.setMideapalyerPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    @OnClick(R.id.im_play)
    public void onClick() {
        Intent intent = new Intent();
        intent.setClass(this, LiveActivity.class);
        intent.putExtra(LiveActivity.LIVE_URL_KEY, farmDetailEntity.getLiveUrl());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

