package com.eego.weibohot;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.WHITE);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowUniversalAccessFromFileURLs(true);
        s.setAllowContentAccess(true);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setTextZoom(100);
        s.setUserAgentString("Mozilla/5.0 (Linux; Android 8.0; Pixel 2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.120 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("localhost/hotsearch")) {
                    return fetchWeibo();
                }
                return super.shouldInterceptRequest(view, request);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/index.html");
    }

    private WebResourceResponse fetchWeibo() {
        HttpURLConnection conn = null;
        try {
            URL weiboUrl = new URL("https://weibo.com/ajax/side/hotSearch");
            conn = (HttpURLConnection) weiboUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.120 Mobile Safari/537.36");
            conn.setRequestProperty("Referer", "https://weibo.com/");
            conn.setRequestProperty("Accept", "application/json, text/plain, */*");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.connect();

            int code = conn.getResponseCode();
            InputStream is;
            if (code >= 200 && code < 300) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
                if (is == null) is = new ByteArrayInputStream(("HTTP " + code).getBytes("UTF-8"));
            }
            WebResourceResponse resp = new WebResourceResponse("application/json", "UTF-8", is);
            resp.setStatusCodeAndReasonPhrase(code, "OK");
            return resp;
        } catch (Exception e) {
            String err = "{\"error\":\"" + e.getMessage() + "\"}";
            try {
                return new WebResourceResponse("application/json", "UTF-8",
                        new ByteArrayInputStream(err.getBytes("UTF-8")));
            } catch (Exception ex) {
                return null;
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
