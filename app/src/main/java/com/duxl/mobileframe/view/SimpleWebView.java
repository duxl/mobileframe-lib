package com.duxl.mobileframe.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.duxl.mobileframe.util.Log;

import java.security.InvalidParameterException;

/**
 * 简单的WebView。对html5的tel://、特殊scheme、https、文件下载等 已做特殊处理<br/>
 * Created by duxl on 2016/09/28.
 */
public class SimpleWebView extends WebView {

    private static final String TAG = "CustomWebView";

    public SimpleWebView(Context context) {
        super(context);
        init();
    }

    public SimpleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SimpleWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settings = getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDomStorageEnabled(true);
        //settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        String userAgent = settings.getUserAgentString();
        try {
            PackageInfo pi = getContext().getPackageManager().getPackageInfo(getContext().getApplicationContext().getPackageName(), 0);
            userAgent += " (app " + pi.packageName+"-Android; versionCode " + pi.versionCode + "; versionName " + pi.versionName + ")";
            settings.setUserAgentString(userAgent);
            Log.d(TAG, "userAgent=" + userAgent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
                SimpleWebView.this.getContext().startActivity(downloadIntent);
            }
        });
    }

    /**
     * 强烈使用{@link WebViewClient}作为参数，因为它已经对一些常规的逻辑做了处理，比如tel:拨打电话，https访问等
     * @param client
     */
    @Override
    public void setWebViewClient(android.webkit.WebViewClient client) {
        if(!(client instanceof WebViewClient)) {
            throw new InvalidParameterException("强烈使用com.duxl.mobileframe.view.SimpleWebView.WebViewClient作为参数，因为它已经对一些常规的逻辑做了处理，比如tel:拨打电话，https访问等");
        }
        super.setWebViewClient(client);
    }

    public static class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "url=" + url);

            String scheme = Uri.parse(url).getScheme();
            if("tel".equals(scheme)) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                view.getContext().startActivity(intent);

            } else if("http".equals(scheme) || "https".equals(scheme)) {
                view.loadUrl(url);

            } else {
                try {
                    // 如果用loadUrl(url);可能不能唤起Activity（自定义scheme）
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }
}
