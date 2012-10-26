package com.sebnarware.avalanche;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
    	
        // enable progress bar for loading, part 1
    	// NOTE must happen before content is added
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        
        WebView webView = (WebView) findViewById(R.id.webview);
        
        // set a reasonable zoom and view mode
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        webView.getSettings().setBuiltInZoomControls(true);
        
        // make link navigation stay within this webview, vs. launching the browser
        webView.setWebViewClient(new WebViewClient());
        
        // enable javascript
        webView.getSettings().setJavaScriptEnabled(true);

		// enable progress bar for loading, part 2
		final Activity self = this;
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// NOTE activities and webviews measure progress with different scales;
				// the progress meter will automatically disappear when we reach 100%
				self.setProgress(progress * 1000);
			}
		});

        // get the desired url from the intent
        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.INTENT_EXTRA_WEB_VIEW_URL);

        
        // set cache mode, depending on current network availability
        if (NetworkEngine.isNetworkAvailable(this)){
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        
        webView.loadUrl(url);
    }

}
