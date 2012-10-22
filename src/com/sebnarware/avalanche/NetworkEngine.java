package com.sebnarware.avalanche;

import android.util.Log;

import com.loopj.android.http.*;

public class NetworkEngine {
	
    private static final String TAG = "NetworkEngine";
    
    // NOTE on threading: "All requests are made outside of your app’s main UI thread, but any callback logic 
    // will be executed on the same thread as the callback was created using Android’s Handler message passing."
	private AsyncHttpClient client = new AsyncHttpClient();
    
    public void loadRegions(JsonHttpResponseHandler responseHandler) {
    	Log.i(TAG, "loadRegions called");
    	client.get("http://aviforecast.herokuapp.com/v1/regions.json", responseHandler);
    }
}
