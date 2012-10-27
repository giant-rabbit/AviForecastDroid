package com.sebnarware.avalanche;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings.ZoomDensity;
import android.app.Activity;

public class DangerScaleActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_scale);
                
        WebView webView = (WebView) findViewById(R.id.webview);
        
        // set a reasonable zoom and view mode
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        webView.getSettings().setBuiltInZoomControls(true);
        
        webView.loadUrl("file:///android_asset/danger_scale_front.jpg");
    }

}
