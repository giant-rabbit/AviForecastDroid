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
    	
    	
    	// get access to the activity indicator
       	// NOTE must happen before content is added
    	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

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
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress == 100) {
			        setProgressBarIndeterminateVisibility(false);
				}
			}
		});
		
	    // NOTE set our user agent string to something benign and non-mobile looking, to work around website
	    // popups from nwac.us asking if you would like to be redirected to the mobile version of the site
		webView.getSettings().setUserAgentString("Mozilla/5.0");
		
        // get the desired url from the intent
        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.INTENT_EXTRA_WEB_VIEW_URL);

        
        // set cache mode, depending on current network availability
        if (NetworkEngine.isNetworkAvailable(this)){
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        
        setProgressBarIndeterminateVisibility(true);
        webView.loadUrl(url);
    }

}
