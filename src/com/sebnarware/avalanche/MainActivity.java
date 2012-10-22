package com.sebnarware.avalanche;

import java.util.Date;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.os.Bundle;

import com.loopj.android.http.*;
import org.json.*;


public class MainActivity extends MapActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
	    
        // testing map view
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);

	    
        
//	    // testing itemized overlay
//	    List<Overlay> mapOverlays = mapView.getOverlays();
//	    Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
//	    RegionItemizedOverlay itemizedoverlay = new RegionItemizedOverlay(drawable, this);
//	    
//	    GeoPoint point = new GeoPoint(19240000,-99120000);
//	    OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
//	    
//	    itemizedoverlay.addOverlay(overlayitem);
//	    mapOverlays.add(itemizedoverlay);

	    
	    // testing overlay
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    GeoPoint point = new GeoPoint(19240000,-99120000);
	    PolygonOverlay overlay = new PolygonOverlay(point, 100000);
	    mapOverlays.add(overlay);

	    
	    
        // testing network and JSON
        final Date startTime = new Date();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://aviforecast.herokuapp.com/v1/regions.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {

                final Date endTime = new Date();
                long millis = endTime.getTime() - startTime.getTime();
                System.out.println("elapsed time: " + millis);

            	try {
                JSONObject firstItem = response.getJSONObject(0);
                String regionId = firstItem.getString("regionId");
                System.out.println(regionId);
            	} catch (JSONException e) {
            		// do nothing
            	}

            }
        });        
    }
    

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

}
