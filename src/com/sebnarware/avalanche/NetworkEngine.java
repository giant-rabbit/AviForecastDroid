package com.sebnarware.avalanche;

import android.util.Log;

import com.loopj.android.http.*;

public class NetworkEngine {
	
    private static final String TAG = "NetworkEngine";
    
    // BUGBUG have not gotten local access to work from either the emulator or from physical devices
//    // local - test data
//    private static final String regionsUrl = "http://10.0.2.2:5000/v1/regions.json";
//    private static final String forecastsUrl = "http://10.0.2.2:5000/v1test/forecasts.json";
//    // local
//    private static final String regionsUrl = "http://10.0.2.2:5000/v1/regions.json";
//    private static final String forecastsUrl = "http://10.0.2.2:5000/v1/forecasts.json";
    // staging - test data
    private static final String regionsUrl = "http://aviforecast-staging.herokuapp.com/v1/regions.json";
    private static final String forecastsUrl = "http://aviforecast-staging.herokuapp.com/v1test/forecasts.json";
//    // staging
//    private static final String regionsUrl = "http://aviforecast-staging.herokuapp.com/v1/regions.json";
//    private static final String forecastsUrl = "http://aviforecast-staging.herokuapp.com/v1/forecasts.json";
//    // production
//    private static final String regionsUrl = "http://aviforecast.herokuapp.com/v1/regions.json";
//    private static final String forecastsUrl = "http://aviforecast.herokuapp.com/v1/forecasts.json";
    
    
    // NOTE on threading: "All requests are made outside of your app’s main UI thread, but any callback logic 
    // will be executed on the same thread as the callback was created using Android’s Handler message passing."
	private AsyncHttpClient client = new AsyncHttpClient();
    
    public void loadRegions(JsonHttpResponseHandler responseHandler) {
    	Log.i(TAG, "loadRegions called");
    	client.get(regionsUrl, responseHandler);
    }
    
    public void loadForecasts(JsonHttpResponseHandler responseHandler) {
    	Log.i(TAG, "loadForecasts called");
    	client.get(forecastsUrl, responseHandler);
    }
}
