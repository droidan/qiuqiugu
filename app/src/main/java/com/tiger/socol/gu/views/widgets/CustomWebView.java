package com.tiger.socol.gu.views.widgets;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tiger.socol.gu.R;

/**
 * WebView的扩展
 * Created by jian_zhou on 2016/4/24.
 */
public class CustomWebView extends WebView {

    private WebSettings wvsetting;

    public CustomWebView(Context context, int defStyle) {
        this(context, null, defStyle);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        instance(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        instance(context);
    }

    public CustomWebView(Context context) {
        super(context);
        instance(context);
    }

    private void instance(Context context) {
        setBackgroundColor(getResources().getColor(R.color.color_ffffff));
        this.setVisibility(View.VISIBLE);
        wvsetting = this.getSettings();
        // 内嵌图片时加载缓慢的问题
        if (Build.VERSION.SDK_INT >= 19) {
            wvsetting.setLoadsImagesAutomatically(true);
        } else {
            wvsetting.setLoadsImagesAutomatically(false);
        }
        // 屏幕适配滚动问题
        wvsetting.setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.4.2; zh-cn;CmsTop Cloud Mobile) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0  Safari/537.36");
        wvsetting.setJavaScriptEnabled(true);// 支持js
        wvsetting.setSupportZoom(false);
        wvsetting.setTextSize(WebSettings.TextSize.NORMAL);
        // wvsetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wvsetting.setAllowFileAccess(true);
        // wvsetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//
        // padding
        wvsetting.setDomStorageEnabled(true);
        wvsetting.setSupportMultipleWindows(true);
        wvsetting.setPluginState(WebSettings.PluginState.ON);
        // ws.setPluginsEnabled(true);
        wvsetting.setLoadWithOverviewMode(true);
        wvsetting.setUseWideViewPort(true);
        wvsetting.setDefaultTextEncodingName("utf-8");
        wvsetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    public void loadWebDataBaseURL(String data) {
        this.loadDataWithBaseURL(null, data, "text/html", "UTF-8", "about:blank");
    }

    public void loadWebDataBaseURL(String url, String data) {
        this.loadDataWithBaseURL(url, data, "text/html", "UTF-8", "about:blank");
    }

    /**
     * 清除缓存
     */
    public void cleanWebCache() {
        this.clearCache(true);
        this.clearHistory();
    }

}
