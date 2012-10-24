package com.sebnarware.avalanche;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends MapActivity implements DataListener {
	
    public final static String INTENT_EXTRA_WEB_VIEW_URL = "com.sebnarware.avalanche.WEB_VIEW_URL";

    private static final String TAG = "MainActivity";

    private static final int DEFAULT_MAP_ZOOM_LEVEL = 8;

    private DataManager dataManager;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.i(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        
        // data stuff (network and JSON)
        dataManager = new DataManager(this);
        dataManager.loadRegions(); 
	    
        
        // map view
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);

	    
	    // add an overlay that draws the blue dot at the user location
	    // NOTE use a overloaded version of MyLocationOverlay to deal with a bug on some phones; see http://joshclemm.com/blog/?p=148
	    myLocationOverlay = new FixedMyLocationOverlay(this, mapView);
	    mapView.getOverlays().add(myLocationOverlay);
	    mapView.invalidate();
	    
	    // when we get a first location fix, pan/zoom the map around the user's location
	    myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
            	// NOTE run this on the UI thread
            	runOnUiThread(new Runnable() {
            		public void run() {
            			// pan and zoom the map
            			mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            			mapView.getController().setZoom(DEFAULT_MAP_ZOOM_LEVEL);
            		}
            	});
            }
        });
    }

	@Override
	protected void onResume() {
    	Log.i(TAG, "onResume called");
		super.onResume();

		// when our activity resumes, we want to start listening for location updates
		myLocationOverlay.enableMyLocation();
	}

	@Override
	protected void onPause() {
    	Log.i(TAG, "onPause called");
		super.onPause();
		
		// when our activity pauses, we want to stop listening for location updates
		myLocationOverlay.disableMyLocation();
	}
	   
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	@Override
	public void regionAdded(RegionData regionData) {
	    PolygonOverlay overlay = new PolygonOverlay(this, regionData);
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    mapOverlays.add(overlay);
	    
	    // force a redraw
	    mapView.invalidate();
	}

	@Override
	public void forecastUpdated(RegionData regionData) {
		
	    // force a redraw
	    mapView.invalidate();
	}

	public void setTimeframeToToday(View view) {
		setTimeframeMode(TimeframeMode.Today);
	}
	
	public void setTimeframeToTomorrow(View view) {
		setTimeframeMode(TimeframeMode.Tomorrow);
	}

	public void setTimeframeToTwoDaysOut(View view) {
		setTimeframeMode(TimeframeMode.TwoDaysOut);
	}

	private void setTimeframeMode(TimeframeMode timeframeMode) {

		Log.i(TAG, "setTimeframeMode setting mode to: " + timeframeMode);
		
		dataManager.setTimeframeMode(timeframeMode);
		
	    // force a redraw
	    mapView.invalidate();
	}

}
