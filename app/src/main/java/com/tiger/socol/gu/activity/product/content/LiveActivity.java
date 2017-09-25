package com.tiger.socol.gu.activity.product.content;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.medialibrary.video.utils.StringUtils;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.base.BaseActivity;
import com.tiger.socol.gu.views.widgets.CustomWebView;

import butterknife.BindView;

public class LiveActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wb_main)
    CustomWebView wbFramContent;

    public static final String LIVE_URL_KEY = "LIVE_URL_KEY";
    public static final String TITLE = "Title";
    private String liveUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        String t = getIntent().getStringExtra(TITLE);
        if (!TextUtils.isEmpty(t)) {
            toolbar.setTitle(t);
        } else {
            toolbar.setTitle("丘丘谷直播");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        liveUrl = getIntent().getStringExtra(LIVE_URL_KEY);
        wbFramContent.loadUrl(liveUrl);
    }

}
