package com.sebnarware.avalanche;

import android.os.Bundle;
import android.webkit.WebView;
import android.app.Activity;
import android.content.Intent;

public class WebViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        
        // Get the desired url from the intent
        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.INTENT_EXTRA_WEB_VIEW_URL);
        
        // get the web view, and load the url
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(url);
    }

}
