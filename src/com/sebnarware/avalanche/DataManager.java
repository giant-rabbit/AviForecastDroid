package com.sebnarware.avalanche;

import java.util.HashMap;

import org.json.*;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DataManager {
	
    private static final String TAG = "DataManager";

	private NetworkEngine networkEngine = new NetworkEngine();
	private HashMap<String, RegionData> regions = new HashMap<String, RegionData>();
	
	public void loadRegions() {
		
		networkEngine.loadRegions(new JsonHttpResponseHandler() {
	        @Override
	        public void onSuccess(JSONArray response) {

	        	Log.i(TAG, "loadRegions network success");
	        	
	        	try {
	        		
	        		// parse out each region
	        		for (int i = 0; i < response.length(); i++) {
	        			JSONObject regionJSON = response.getJSONObject(i);
	        			String regionId = regionJSON.getString("regionId");
	        			String displayName = regionJSON.getString("displayName");
	        			String URL = regionJSON.getString("URL");
	        			
	        			JSONArray pointsJSON = regionJSON.getJSONArray("points");
	        			GeoPoint[] polygon = new GeoPoint[pointsJSON.length()];
	        			for (int j = 0; j < pointsJSON.length(); j++) {
	        				JSONObject pointJSON = pointsJSON.getJSONObject(j);
	        				double lat = pointJSON.getDouble("lat");
	        				double lon = pointJSON.getDouble("lon");
	        				polygon[j] = new GeoPoint((int)(lat * 1000000), (int)(lon * 1000000));
	        			}
	        			
	        			// create the region data, and add it to our set
	        			RegionData regionData = new RegionData(regionId, displayName, URL, polygon);
	        			regions.put(regionId, regionData);
		        		Log.i(TAG, "loadRegions created region: " + regionId);
	        		}
	        		
	        		Log.i(TAG, "loadRegions total count of regions created: " + regions.size());

	        	} catch (JSONException e) {
	            	Log.w(TAG, "loadRegions JSON parsing failure");
	        	}
	        }
	        
	        @Override
	        public void onFailure(Throwable error, String content) {
	        	Log.w(TAG, "loadRegions network failure");
	        }
	    });
		

	}
}
