package com.sebnarware.avalanche;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        
        // Get the desired url from the intent
        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.INTENT_EXTRA_WEB_VIEW_URL);
        
        WebView webView = (WebView) findViewById(R.id.webview);
        
        // set a reasonable zoom and view mode
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        webView.getSettings().setBuiltInZoomControls(true);
        
        // make link navigation stay within this webview, vs. launching the browser
        webView.setWebViewClient(new WebViewClient());
        
        // enable javascript
        webView.getSettings().setJavaScriptEnabled(true);
        
        webView.loadUrl(url);
    }

}
