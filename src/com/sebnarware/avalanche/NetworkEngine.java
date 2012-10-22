package com.sebnarware.avalanche;

import org.json.*;

import android.util.Log;

import com.loopj.android.http.*;

public class NetworkEngine {
	
    private static final String TAG = "NetworkEngine";
    
	private AsyncHttpClient client = new AsyncHttpClient();
    
    public void loadRegions() {

    	Log.i(TAG, "loadRegions called");

    	client.get("http://aviforecast.herokuapp.com/v1XXX/regions.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {

            	Log.i(TAG, "loadRegions success");
            	
            	try {
                JSONObject firstItem = response.getJSONObject(0);
                String regionId = firstItem.getString("regionId");
                Log.i(TAG, regionId);
            	} catch (JSONException e) {
            		// do nothing
            	}
            }
            
            @Override
            public void onFailure(Throwable error, String content) {
            	Log.w(TAG, "loadRegions failure");
            }
        });
    }

}
