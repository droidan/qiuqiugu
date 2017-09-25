package com.tiger.socol.gu.activity.product.content;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.medialibrary.video.JCVideoPlayer;
import com.medialibrary.video.listener.ScreenListener;
import com.medialibrary.video.utils.ScreenSwitchUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.product.DemoAdapter;
import com.tiger.socol.gu.activity.product.SpacesItemDecoration;
import com.tiger.socol.gu.activity.shopCart.order.CartContentActivity;
import com.tiger.socol.gu.api.good.GoodDetailEntity;
import com.tiger.socol.gu.api.good.VideoEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.Config;
import com.tiger.socol.gu.views.widgets.CustomWebView;
import com.tiger.socol.gu.views.widgets.FullyLinearLayoutManager;
import com.tiger.socol.gu.views.widgets.VideoPlayerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductContentActivity extends BaseViewStateActivity<ProductContentView, ProductContentPresenter>
        implements ProductContentView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tx_fram_name)
    TextView txFramName;
    @BindView(R.id.wb_fram_conten)
    CustomWebView wbFramContent;
    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.tx_product_price)
    TextView txProductPrice;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.bt_collect)
    TextView btCollect;
    @BindView(R.id.linear_collect)
    LinearLayout linear_collect;
    @BindView(R.id.iv_product_collect)
    ImageView iv_product_collect;
    @BindView(R.id.custom_videoplayer)
    VideoPlayerView viewDetailLesson;
    @BindView(R.id.sl_main)
    ScrollView slMain;
    @BindView(R.id.mb_start)
    MaterialRatingBar mbStart;
    @BindView(R.id.im_thumb)
    ImageView imThumb;
    @BindView(R.id.im_play)
    ImageView imPlay;
    @BindView(R.id.rl_thumb)
    RelativeLayout rlThumb;

    private DemoAdapter demoAdapter;
    private CommentAdapter commentAdapter;
    private int goodsId = 0;
    private boolean collected = false;
    private GoodDetailEntity goodDetailEntity;

    public static final String PRODUCT_ID_KEY = "PRODUCT_ID_KEY";

    @NonNull
    @Override
    public ProductContentPresenter createPresenter() {
        return new ProductContentPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_content;
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
        goodsId = getIntent().getIntExtra(PRODUCT_ID_KEY, 0);
        presenter.requestContent(goodsId);
    }

    private void getDisplayInfomation() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
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

        viewDetailLesson.setScreenListener(screenListener);

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            wbFramContent.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wbFramContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            wbFramContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        // 评论
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        rvComment.setLayoutManager(linearLayoutManager);
        rvComment.setNestedScrollingEnabled(false);
        commentAdapter = new CommentAdapter();
        rvComment.setAdapter(commentAdapter);

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

    @OnClick({R.id.bt_show_comment, R.id.bt_call_phone, R.id.bt_farm, R.id.linear_collect, R.id.bt_add_cart, R.id.bt_buy, R.id.im_play})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.bt_show_comment:
                // 显示更多评论
                intent = new Intent();
                intent.setClass(this, CommentListActivity.class);
                intent.putExtra(IntentConstant.GOOD_ID, goodsId);
                intent.putExtra(CommentListActivity.GOOD_START_KEY, goodDetailEntity.getStars());
                intent.putExtra(CommentListActivity.GOOD_THUMBS_KEY, goodDetailEntity.getThumb());
                intent.putExtra(CommentListActivity.GOOD_NAME_KEY, goodDetailEntity.getGoodsName());
                startActivity(intent);
                break;

            case R.id.bt_call_phone:
                // 客服
                presenter.callPhone(this);
                break;

            case R.id.bt_farm:
                if (goodDetailEntity == null) return;
                // 基地
                intent = new Intent();
                intent.setClass(this, FarmContentActivity.class);
                intent.putExtra(FarmContentActivity.FARM_CONTENT_ID_KEY, goodDetailEntity.getBaseId());
                startActivity(intent);
                break;

            case R.id.linear_collect:
                // 收藏
                if (!MemberMannager.checkLogin(this)) {
                    return;
                }
                presenter.collectGood(goodsId, !collected);
                break;

            case R.id.bt_add_cart:
                // 添加到购物车
                if (!MemberMannager.checkLogin(this)) {
                    return;
                }
                presenter.addCart(goodsId);
                break;

            case R.id.bt_buy:
                // 立即购买
                if (!MemberMannager.checkLogin(this)) {
                    return;
                }
                ArrayList<GoodsEntity> goods = new ArrayList<>();
                GoodsEntity good = goodDetailEntity;
                good.setNum(1);
                goods.add(good);

                intent = new Intent();
                intent.setClass(this, CartContentActivity.class);
                intent.putParcelableArrayListExtra(CartContentActivity.SELECT_GOODS_KEY,
                        goods);
                startActivity(intent);
                break;

            case R.id.im_play:
                intent = new Intent();
                intent.setClass(this, LiveActivity.class);
                intent.putExtra(LiveActivity.LIVE_URL_KEY, goodDetailEntity.getLiveUrl());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestContentSuccess(GoodDetailEntity entity) {
        dismissProgress();
        slMain.fullScroll(ScrollView.FOCUS_UP);

        goodDetailEntity = entity;
        mbStart.setRating((float) entity.getStars());
        changeCollectStatus(entity.isCollected());

        // 设置封面
        String live = entity.getLiveUrl();
        if (StringUtils.isEmpty(live)) {
            imPlay.setVisibility(View.GONE);
        } else {
            imPlay.setVisibility(View.VISIBLE);
        }
        Picasso.with(this).load(entity.getThumb()).into(imThumb);

        txFramName.setText(entity.getGoodsName());
        txProductPrice.setText("￥" + entity.getPrice());
        wbFramContent.loadWebDataBaseURL(Config.URL, entity.getContent());

        // 设置评论
        if (entity.getComments().getData().size() == 0) {
            llComment.setVisibility(View.GONE);
        } else {
            llComment.setVisibility(View.VISIBLE);
            commentAdapter.setComments(entity.getComments().getData());
        }

        demoAdapter.setGoods(entity.getRelated().getData());

        if (entity.getLive() == null) {
            return;
        }
        releaseMediaPlayer();
        initVideoData(entity.getLive());
    }

    private void initVideoData(VideoEntity videoInfo) {
        viewDetailLesson.setVideoInfo(videoInfo);
    }

    private void changeCollectStatus(boolean collected) {
        this.collected = collected;
        if (collected) {
            btCollect.setText("已收藏");
            iv_product_collect.setImageResource(R.mipmap.collection);
        } else {
            btCollect.setText("收藏");
            iv_product_collect.setImageResource(R.mipmap.no_collection);
        }
    }

    @Override
    public void onRequestContentFailure(String message) {
        showToask(message);
    }

    @Override
    public void onCollectSuccess(boolean collect) {
        changeCollectStatus(collect);
    }

    @Override
    public void onCollectFailure(String message) {
        showToask("操作失败");
    }

    @Override
    public void onAddCartSuccess(String message) {
        showToask(message);
    }


    @Override
    public void onAddCaetFailure(String message) {
        showToask(message);
    }

    //-------------------------------视频---------------------------------------------

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
                instance = new ScreenSwitchUtils(ProductContentActivity.this);
            instance.start(ProductContentActivity.this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.im_play)
    public void onClick() {
    }
}

